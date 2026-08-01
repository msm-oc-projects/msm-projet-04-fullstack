import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { User } from '../../core/models/user.interface';
import { SessionService } from '../../core/service/session.service';
import { UserService } from '../../core/service/user.service';
import { MeComponent } from './me.component';

describe('MeComponent integration', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  const user: User = {
    id: 1,
    email: 'user@example.com',
    firstName: 'First',
    lastName: 'Last',
    admin: false,
    password: '',
    createdAt: new Date()
  };
  const userService = {
    getById: jest.fn(() => of(user)),
    delete: jest.fn(() => of(undefined))
  };
  const sessionService = {
    sessionInformation: { id: 1, admin: false },
    logOut: jest.fn()
  };
  const router = { navigate: jest.fn() };
  const snackBar = { open: jest.fn() };

  beforeEach(async () => {
    jest.clearAllMocks();
    TestBed.overrideProvider(MatSnackBar, { useValue: snackBar });
    await TestBed.configureTestingModule({
      imports: [MeComponent],
      providers: [
        { provide: UserService, useValue: userService },
        { provide: SessionService, useValue: sessionService },
        { provide: Router, useValue: router },
        { provide: MatSnackBar, useValue: snackBar }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('loads and displays the connected user', () => {
    expect(userService.getById).toHaveBeenCalledWith('1');
    expect(component.user).toEqual(user);
    expect(fixture.nativeElement.textContent).toContain('user@example.com');
  });

  it('deletes the account and logs out', () => {
    component.delete();
    expect(userService.delete).toHaveBeenCalledWith('1');
    expect(snackBar.open).toHaveBeenCalled();
    expect(sessionService.logOut).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(['/']);
  });

  it('goes back in browser history', () => {
    const back = jest.spyOn(window.history, 'back').mockImplementation();
    component.back();
    expect(back).toHaveBeenCalled();
    back.mockRestore();
  });
});
