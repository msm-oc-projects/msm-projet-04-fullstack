const member = {
  id: 1,
  email: 'user@example.com',
  firstName: 'First',
  lastName: 'Last',
  admin: false,
  createdAt: '2026-06-01T10:00:00',
  updatedAt: '2026-06-02T10:00:00'
};

describe('Account and navigation screens', () => {
  it('displays an admin account', () => {
    cy.intercept('GET', '/api/session', []);
    cy.intercept('GET', '/api/user/1', { ...member, admin: true }).as('user');
    cy.login(true);

    cy.contains('Account').click();
    cy.wait('@user');
    cy.contains('You are admin').should('be.visible');
  });

  it('deletes a member account', () => {
    cy.intercept('GET', '/api/session', []);
    cy.intercept('GET', '/api/user/1', member).as('user');
    cy.intercept('DELETE', '/api/user/1', {}).as('deleteUser');
    cy.login();

    cy.contains('Account').click();
    cy.wait('@user');
    cy.contains('Delete my account').should('be.visible');
    cy.get('button[color=warn]').click();
    cy.wait('@deleteUser');
    cy.url().should('include', '/login');
  });

  it('redirects an unknown route to the not-found screen', () => {
    cy.visit('/unknown-page');
    cy.url().should('include', '/404');
    cy.contains('Page not found').should('be.visible');
  });
});
