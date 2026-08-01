import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { LoginRequest } from '../models/loginRequest.interface';
import { RegisterRequest } from '../models/registerRequest.interface';
import { SessionInformation } from '../models/sessionInformation.interface';
import { AuthService } from './auth.service';

describe('AuthService', () => {
  let service: AuthService;
  let httpTesting: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()]
    });
    service = TestBed.inject(AuthService);
    httpTesting = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpTesting.verify());

  it('registers a user', () => {
    const request: RegisterRequest = {
      email: 'user@example.com',
      firstName: 'First',
      lastName: 'Last',
      password: 'password'
    };

    service.register(request).subscribe(response => expect(response).toBeNull());

    const httpRequest = httpTesting.expectOne('/api/auth/register');
    expect(httpRequest.request.method).toBe('POST');
    expect(httpRequest.request.body).toEqual(request);
    httpRequest.flush(null);
  });

  it('logs a user in', () => {
    const request: LoginRequest = { email: 'user@example.com', password: 'password' };
    const response: SessionInformation = {
      token: 'token',
      type: 'Bearer',
      id: 1,
      username: request.email,
      firstName: 'First',
      lastName: 'Last',
      admin: false
    };

    service.login(request).subscribe(session => expect(session).toEqual(response));

    const httpRequest = httpTesting.expectOne('/api/auth/login');
    expect(httpRequest.request.method).toBe('POST');
    httpRequest.flush(response);
  });
});
