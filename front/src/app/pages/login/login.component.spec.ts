import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { SessionInformation } from '../../core/models/sessionInformation.interface';
import { AuthService } from '../../core/service/auth.service';
import { SessionService } from '../../core/service/session.service';
import { LoginComponent } from './login.component';

describe('LoginComponent integration', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  const authService = { login: jest.fn() };
  const sessionService = { logIn: jest.fn() };
  const router = { navigate: jest.fn() };

  beforeEach(async () => {
    jest.clearAllMocks();
    await TestBed.configureTestingModule({
      imports: [LoginComponent],
      providers: [
        { provide: AuthService, useValue: authService },
        { provide: SessionService, useValue: sessionService },
        { provide: Router, useValue: router }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('keeps submit disabled while the form is invalid', () => {
    const button = fixture.nativeElement.querySelector('button[type="submit"]') as HTMLButtonElement;
    expect(component.form.invalid).toBe(true);
    expect(button.disabled).toBe(true);
  });

  it('logs in and navigates to sessions', () => {
    const response: SessionInformation = {
      token: 'token',
      type: 'Bearer',
      id: 1,
      username: 'user@example.com',
      firstName: 'First',
      lastName: 'Last',
      admin: false
    };
    authService.login.mockReturnValue(of(response));
    component.form.setValue({ email: response.username, password: 'password' });

    component.submit();

    expect(authService.login).toHaveBeenCalledWith({
      email: response.username,
      password: 'password'
    });
    expect(sessionService.logIn).toHaveBeenCalledWith(response);
    expect(router.navigate).toHaveBeenCalledWith(['/sessions']);
  });

  it('displays an error when authentication fails', () => {
    authService.login.mockReturnValue(throwError(() => new Error('Unauthorized')));
    component.form.setValue({ email: 'user@example.com', password: 'password' });

    component.submit();
    fixture.detectChanges();

    expect(component.onError).toBe(true);
    expect(fixture.nativeElement.querySelector('.error').textContent).toContain('An error occurred');
  });
});
