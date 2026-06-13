import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { SessionService } from '../core/service/session.service';
import { AuthGuard } from './auth.guard';

describe('AuthGuard', () => {
  const router = { navigate: jest.fn() };
  const sessionService = { isLogged: false };
  let guard: AuthGuard;

  beforeEach(() => {
    router.navigate.mockClear();
    TestBed.configureTestingModule({
      providers: [
        AuthGuard,
        { provide: Router, useValue: router },
        { provide: SessionService, useValue: sessionService }
      ]
    });
    guard = TestBed.inject(AuthGuard);
  });

  it('rejects unauthenticated users', () => {
    sessionService.isLogged = false;
    expect(guard.canActivate()).toBe(false);
    expect(router.navigate).toHaveBeenCalledWith(['login']);
  });

  it('accepts authenticated users', () => {
    sessionService.isLogged = true;
    expect(guard.canActivate()).toBe(true);
    expect(router.navigate).not.toHaveBeenCalled();
  });
});
