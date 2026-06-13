import { HttpRequest, HttpResponse } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { SessionService } from '../core/service/session.service';
import { customJwtInterceptorFn } from './customJwtInterceptorFn';

describe('customJwtInterceptorFn', () => {
  const next = jest.fn(request => of(new HttpResponse({ body: request })));

  beforeEach(() => {
    next.mockClear();
    TestBed.resetTestingModule();
  });

  it('forwards anonymous requests unchanged', () => {
    const sessionService = { isLogged: false };
    const request = new HttpRequest('GET', '/api/session');

    TestBed.configureTestingModule({
      providers: [{ provide: SessionService, useValue: sessionService }]
    });
    TestBed.runInInjectionContext(() => customJwtInterceptorFn(request, next).subscribe());

    expect(next).toHaveBeenCalledWith(request);
  });

  it('adds the bearer token for authenticated requests', () => {
    const sessionService = {
      isLogged: true,
      sessionInformation: { token: 'jwt-token' }
    };
    const request = new HttpRequest('GET', '/api/session');

    TestBed.configureTestingModule({
      providers: [{ provide: SessionService, useValue: sessionService }]
    });
    TestBed.runInInjectionContext(() => customJwtInterceptorFn(request, next).subscribe());

    const forwarded = next.mock.calls[0][0] as HttpRequest<unknown>;
    expect(forwarded.headers.get('Authorization')).toBe('Bearer jwt-token');
  });
});
