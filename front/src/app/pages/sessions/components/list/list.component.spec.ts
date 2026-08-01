import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { of } from 'rxjs';
import { Session } from 'src/app/core/models/session.interface';
import { SessionApiService } from 'src/app/core/service/session-api.service';
import { SessionService } from 'src/app/core/service/session.service';

import { ListComponent } from './list.component';

describe('ListComponent integration', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;

  const sessions: Session[] = [{
    id: 10,
    name: 'Morning yoga',
    description: 'A gentle session',
    date: new Date('2026-06-13'),
    teacher_id: 2,
    users: []
  }];

  const sessionApiService = {
    all: jest.fn(() => of(sessions))
  };

  const mockSessionService = {
    sessionInformation: {
      admin: false
    }
  };

  beforeEach(async () => {
    jest.clearAllMocks();
    mockSessionService.sessionInformation.admin = false;
    await TestBed.configureTestingModule({
      imports: [
        ListComponent,
        RouterTestingModule
      ],
      providers: [
        { provide: SessionApiService, useValue: sessionApiService },
        { provide: SessionService, useValue: mockSessionService }
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('displays the sessions and Detail action to a standard user', () => {
    expect(component).toBeTruthy();
    expect(sessionApiService.all).toHaveBeenCalledTimes(1);
    expect(fixture.nativeElement.textContent).toContain('Morning yoga');
    expect(fixture.nativeElement.textContent).toContain('A gentle session');
    expect(fixture.nativeElement.textContent).toContain('Detail');
    expect(fixture.nativeElement.textContent).not.toContain('Create');
    expect(fixture.nativeElement.textContent).not.toContain('Edit');
  });

  it('displays Create, Detail and Edit actions to an admin', () => {
    mockSessionService.sessionInformation.admin = true;
    fixture.detectChanges();

    expect(fixture.nativeElement.textContent).toContain('Create');
    expect(fixture.nativeElement.textContent).toContain('Detail');
    expect(fixture.nativeElement.textContent).toContain('Edit');
  });
});
