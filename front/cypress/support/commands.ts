declare global {
  namespace Cypress {
    interface Chainable {
      login(admin?: boolean): Chainable<void>;
    }
  }
}

Cypress.Commands.add('login', (admin = false) => {
  cy.intercept('POST', '/api/auth/login', {
    statusCode: 200,
    body: {
      token: 'jwt-token',
      type: 'Bearer',
      id: 1,
      username: 'user@example.com',
      firstName: 'First',
      lastName: 'Last',
      admin
    }
  }).as('login');

  cy.visit('/login');
  cy.get('input[formControlName=email]').type('user@example.com');
  cy.get('input[formControlName=password]').type('password');
  cy.get('button[type=submit]').click();
  cy.wait('@login');
});

export {};
