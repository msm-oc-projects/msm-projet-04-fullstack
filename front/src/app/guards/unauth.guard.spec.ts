import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { SessionService } from '../core/service/session.service';
import { UnauthGuard } from './unauth.guard';

describe('UnauthGuard', () => {
  const router = { navigate: jest.fn() };
  const sessionService = { isLogged: false };
  let guard: UnauthGuard;

  beforeEach(() => {
    router.navigate.mockClear();
    TestBed.configureTestingModule({
      providers: [
        UnauthGuard,
        { provide: Router, useValue: router },
        { provide: SessionService, useValue: sessionService }
      ]
    });
    guard = TestBed.inject(UnauthGuard);
  });

  it('accepts unauthenticated users', () => {
    sessionService.isLogged = false;
    expect(guard.canActivate()).toBe(true);
  });

  it('rejects authenticated users', () => {
    sessionService.isLogged = true;
    expect(guard.canActivate()).toBe(false);
    expect(router.navigate).toHaveBeenCalledWith(['rentals']);
  });
});
