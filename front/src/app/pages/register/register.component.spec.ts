import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { AuthService } from '../../core/service/auth.service';
import { RegisterComponent } from './register.component';

describe('RegisterComponent integration', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  const authService = { register: jest.fn() };
  const router = { navigate: jest.fn() };

  beforeEach(async () => {
    jest.clearAllMocks();
    await TestBed.configureTestingModule({
      imports: [RegisterComponent],
      providers: [
        { provide: AuthService, useValue: authService },
        { provide: Router, useValue: router }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('registers and navigates to login', () => {
    authService.register.mockReturnValue(of(undefined));
    component.form.setValue({
      email: 'user@example.com',
      firstName: 'First',
      lastName: 'Last',
      password: 'password'
    });

    component.submit();

    expect(authService.register).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  });

  it('displays an error when registration fails', () => {
    authService.register.mockReturnValue(throwError(() => new Error('Conflict')));
    component.form.setValue({
      email: 'user@example.com',
      firstName: 'First',
      lastName: 'Last',
      password: 'password'
    });

    component.submit();
    fixture.detectChanges();

    expect(component.onError).toBe(true);
    expect(fixture.nativeElement.querySelector('.error').textContent).toContain('An error occurred');
  });
});
