import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { BehaviorSubject } from 'rxjs';
import { SessionService } from './core/service/session.service';
import { AuthService } from './core/service/auth.service';
import { AppComponent } from './app.component';

describe('AppComponent integration', () => {
  let fixture: ComponentFixture<AppComponent>;
  let component: AppComponent;
  const loggedSubject = new BehaviorSubject(false);
  const sessionService = {
    $isLogged: jest.fn(() => loggedSubject.asObservable()),
    logOut: jest.fn()
  };
  let router: Router;

  beforeEach(async () => {
    jest.clearAllMocks();
    loggedSubject.next(false);
    await TestBed.configureTestingModule({
      imports: [AppComponent, RouterTestingModule],
      providers: [
        { provide: SessionService, useValue: sessionService },
        { provide: AuthService, useValue: {} }
      ]
    }).compileComponents();
    router = TestBed.inject(Router);
    jest.spyOn(router, 'navigate').mockResolvedValue(true);
    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('shows login and register links for anonymous users', () => {
    expect(fixture.nativeElement.textContent).toContain('Login');
    expect(fixture.nativeElement.textContent).toContain('Register');
  });

  it('shows account links for authenticated users', () => {
    loggedSubject.next(true);
    fixture.detectChanges();
    expect(fixture.nativeElement.textContent).toContain('Sessions');
    expect(fixture.nativeElement.textContent).toContain('Account');
    expect(fixture.nativeElement.textContent).toContain('Logout');
  });

  it('logs out and returns to the home page', () => {
    component.logout();
    expect(sessionService.logOut).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(['']);
  });
});
