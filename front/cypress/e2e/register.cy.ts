describe('Register spec', () => {
  const emailInput = 'input[formControlName=email]';
  const passwordInput = 'input[formControlName=password]';
  const firstName = 'input[formControlName=firstName]';
  const lastName = 'input[formControlName=lastName]';

  const mockUser = {
    id: 1,
    username: 'userName',
    firstName: 'firstName',
    lastName: 'lastName',
    admin: true
  };
  const mockSession1 = {
    "id": 1,
    "name": "Tryng session, enjoy!",
    "date": "2023-05-24T00:00:00.000+00:00",
    "teacher_id": 1,
    "description": "Learn best practices for yoga",
    "users": [2, 3],
    "createdAt": "2023-05-23T17:22:01",
    "updatedAt": "2023-05-23T17:29:21"
  };
  const mockSession2 = {
    "id": 3,
    "name": "Tryng session, enjoy!",
    "date": "2023-05-24T00:00:00.000+00:00",
    "teacher_id": 1,
    "description": "Learn best practices for yoga and find peace",
    "users": [2, 3],
    "createdAt": "2023-05-23T17:22:01",
    "updatedAt": "2023-05-23T17:29:21"
  };

  it('Register successfully and login', () => {
    cy.visit("/register")
    cy.intercept({
      method: 'POST',
      url: '/api/auth/register',
    },
      {
        body: {
          message: "User registered successfully!"
        },
      })

    cy.intercept({
      method: 'POST',
      url: '/api/auth/login',
    },
      {
        body: mockUser,
      })

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      {
        body: [mockSession1, mockSession2]
      },
      []).as('session')

    cy.get(firstName).type("Good")
    cy.get(lastName).type("User")
    cy.get(emailInput).type("test@test.com")
    cy.get(passwordInput).type(`${"test!1234"}{enter}{enter}`)

    cy.url().should("include", "/login")

    cy.get(emailInput).type("test@test.com")
    cy.get(passwordInput).type(`${"test!1234"}{enter}{enter}`)

    cy.url().should('include', '/sessions')

  })

  it('Register: email or password invalid', () => {
    cy.visit('/register')

    cy.get(firstName).type("Good")
    cy.get(lastName).type("User")
    cy.get(emailInput).type("yoga@studio.com")
    cy.get(passwordInput).type(`${"test!1234"}{enter}{enter}`)
    cy.get('.error').contains("An error occurred")
  })

  it('should return error if email already used', () => {
    cy.visit('/register')

    cy.intercept('POST', '/api/auth/register', {statusCode: 400})

    cy.get(firstName).type("firstname")
    cy.get(lastName).type("lastname")
    cy.get(emailInput).type("new@user.com")
    cy.get(passwordInput).type(`${"wrongpass"}{enter}{enter}`)
    cy.get('span[data-test-id=error-register]').should('be.visible');
  })
});


