const session = {
  id: 10,
  name: 'Morning yoga',
  date: '2026-06-13',
  description: 'A calm session',
  teacher_id: 2,
  users: [],
  createdAt: '2026-06-01T10:00:00',
  updatedAt: '2026-06-02T10:00:00'
};

const teacher = {
  id: 2,
  firstName: 'First',
  lastName: 'Teacher',
  createdAt: '2026-01-01T10:00:00',
  updatedAt: '2026-01-01T10:00:00'
};

describe('Session screens', () => {
  it('lists sessions for a member', () => {
    cy.intercept('GET', '/api/session', [session]).as('sessions');
    cy.login();
    cy.wait('@sessions');

    cy.contains('Morning yoga').should('be.visible');
    cy.contains('Create').should('not.exist');
    cy.contains('Edit').should('not.exist');
  });

  it('lists admin actions', () => {
    cy.intercept('GET', '/api/session', [session]).as('sessions');
    cy.login(true);
    cy.wait('@sessions');

    cy.contains('Create').should('be.visible');
    cy.contains('Edit').should('be.visible');
  });

  it('displays a session and allows participation', () => {
    cy.intercept('GET', '/api/session', [session]);
    cy.intercept('GET', '/api/session/10', session).as('detail');
    cy.intercept('GET', '/api/teacher/2', teacher).as('teacher');
    cy.intercept('POST', '/api/session/10/participate/1', {}).as('participate');
    cy.login();

    cy.contains('Detail').click();
    cy.wait(['@detail', '@teacher']);
    cy.contains('Morning Yoga').should('be.visible');
    cy.contains('Participate').click();
    cy.wait('@participate');
  });

  it('allows a participant to leave a session', () => {
    const participatedSession = { ...session, users: [1] };
    cy.intercept('GET', '/api/session', [participatedSession]);
    cy.intercept('GET', '/api/session/10', participatedSession).as('detail');
    cy.intercept('GET', '/api/teacher/2', teacher);
    cy.intercept('DELETE', '/api/session/10/participate/1', {}).as('leave');
    cy.login();

    cy.contains('Detail').click();
    cy.wait('@detail');
    cy.contains('Do not participate').click();
    cy.wait('@leave');
  });

  it('allows an admin to delete a session', () => {
    cy.intercept('GET', '/api/session', [session]);
    cy.intercept('GET', '/api/session/10', session);
    cy.intercept('GET', '/api/teacher/2', teacher);
    cy.intercept('DELETE', '/api/session/10', {}).as('deleteSession');
    cy.login(true);

    cy.contains('Detail').click();
    cy.contains('Delete').click();
    cy.wait('@deleteSession');
    cy.url().should('include', '/sessions');
  });

  it('creates a session', () => {
    cy.intercept('GET', '/api/session', []);
    cy.intercept('GET', '/api/teacher', [teacher]).as('teachers');
    cy.intercept('POST', '/api/session', session).as('createSession');
    cy.login(true);

    cy.contains('Create').click();
    cy.wait('@teachers');
    cy.get('input[formControlName=name]').type('Morning yoga');
    cy.get('input[formControlName=date]').type('2026-06-13');
    cy.get('mat-select[formControlName=teacher_id]').click();
    cy.contains('First Teacher').click();
    cy.get('textarea[formControlName=description]').type('A calm session');
    cy.get('button[type=submit]').click();

    cy.wait('@createSession');
    cy.url().should('include', '/sessions');
  });

  it('updates a session', () => {
    cy.intercept('GET', '/api/session', [session]);
    cy.intercept('GET', '/api/teacher', [teacher]);
    cy.intercept('GET', '/api/session/10', session).as('detail');
    cy.intercept('PUT', '/api/session/10', { ...session, name: 'Updated yoga' }).as('updateSession');
    cy.login(true);

    cy.contains('Edit').click();
    cy.wait('@detail');
    cy.get('input[formControlName=name]').clear().type('Updated yoga');
    cy.get('button[type=submit]').click();

    cy.wait('@updateSession');
    cy.url().should('include', '/sessions');
  });
});
