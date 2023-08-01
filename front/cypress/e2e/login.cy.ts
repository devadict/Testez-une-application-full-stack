describe('Login spec', () => {
  const emailInput = 'input[formControlName=email]';
  const passwordInput = 'input[formControlName=password]';
  const submitButton = 'button[type=submit]';
  const errorParagraph = 'p[class="error ng-star-inserted"]';
  const logoutButton = 'span[data-test-id=logout-btn]';

  beforeEach(() => {
    cy.visit('/login');
  });

  it('Login the user in successfully', () => {
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    });

    cy.intercept('GET', '/api/session', []).as('session');

    cy.get(emailInput).type("yoga@studio.com");
    cy.get(passwordInput).type(`${"test!1234"}{enter}{enter}`);

    cy.url().should('include', '/sessions');
  });
  
  it('Logout the user in successfully', () => {
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    });

    cy.intercept('GET', '/api/session', []).as('session');

    cy.get(emailInput).type("yoga@studio.com");
    cy.get(passwordInput).type(`${"test!1234"}{enter}{enter}`);

    cy.url().should('include', '/sessions');
    cy.get('span[data-testid=logout-button]').click();
  });

  it('Failed login, password too short', () => {

    cy.intercept('POST', '/api/auth/login', {
      statusCode: 400,
      body: {
        message: 'Unauthorized',
      },
    })

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session')

    cy.get(emailInput).type("fake@studio.com")
    cy.get(passwordInput).type(`${" "}{enter}{enter}`)

    cy.url().should('include', '/login')
    cy.get(errorParagraph).should('contain', 'An error occurred')
  });

  it('should disable submit button because of no password', () => {
    cy.get(passwordInput).clear;
    cy.get(emailInput).type(`${"new@account.com"}{enter}{enter}`);
    cy.get(passwordInput).should('have.class', 'ng-invalid');
    cy.get(submitButton).should('be.disabled');
  });
 
});