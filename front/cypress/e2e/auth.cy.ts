describe('Authentication screens', () => {
  it('registers a new account', () => {
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 200,
      body: { message: 'User registered successfully!' }
    }).as('register');

    cy.visit('/register');
    cy.get('input[formControlName=firstName]').type('First');
    cy.get('input[formControlName=lastName]').type('Last');
    cy.get('input[formControlName=email]').type('user@example.com');
    cy.get('input[formControlName=password]').type('password');
    cy.get('button[type=submit]').click();

    cy.wait('@register');
    cy.url().should('include', '/login');
  });

  it('shows a registration error', () => {
    cy.intercept('POST', '/api/auth/register', { statusCode: 400 }).as('register');

    cy.visit('/register');
    cy.get('input[formControlName=firstName]').type('First');
    cy.get('input[formControlName=lastName]').type('Last');
    cy.get('input[formControlName=email]').type('existing@example.com');
    cy.get('input[formControlName=password]').type('password');
    cy.get('button[type=submit]').click();

    cy.wait('@register');
    cy.get('.error').should('contain', 'An error occurred');
  });

  it('logs in and logs out', () => {
    cy.intercept('GET', '/api/session', []).as('sessions');
    cy.login();
    cy.wait('@sessions');
    cy.url().should('include', '/sessions');

    cy.contains('Logout').click();
    cy.url().should('include', '/login');
  });

  it('shows a login error', () => {
    cy.intercept('POST', '/api/auth/login', { statusCode: 401 }).as('login');

    cy.visit('/login');
    cy.get('input[formControlName=email]').type('user@example.com');
    cy.get('input[formControlName=password]').type('wrong-password');
    cy.get('button[type=submit]').click();

    cy.wait('@login');
    cy.get('.error').should('contain', 'An error occurred');
  });
});
