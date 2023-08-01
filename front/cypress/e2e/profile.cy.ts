describe('User details', () => {
  const emailInput = 'input[formControlName=email]';
  const passwordInput = 'input[formControlName=password]';
  const mockUser = {
    id: 1,
    username: 'userName',
    firstName: 'firstName',
    lastName: 'lastName',
    admin: true,
    token: "token"
  };
  const mockSession1 = {
    "id": 1,
    "name": "Trying session",
    "date": "2023-05-19T00:00:00.000+00:00",
    "teacher_id": 1,
    "description": "Yoga Basics",
    "users": [2, 3],
    "createdAt": "2023-05-23T11:22:01",
    "updatedAt": "2023-05-23T11:29:21"
  };
  const mockSession2 =
    {
      "id": 3,
      "name": "Second sessionr",
      "date": "2023-05-19T00:00:00.000+00:00",
      "teacher_id": 1,
      "description": "Late ight session",
      "users": [],
      "createdAt": "2023-05-23T15:24:33",
      "updatedAt": "2023-05-23T15:24:33"
  };

  it('me', () => {
    cy.visit('/login')

    cy.intercept({
        method: 'POST',
        url: '/api/auth/login',
      },
      {
        body: mockUser,
      }, []).as("login")

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      {
        body: [mockSession1,mockSession2]
      },
      []).as('session')

    cy.intercept({
      method: "GET",
      url: '/api/user/1',
    }, {
      body: {
        admin: true,
        createdAt:"2023-05-11T19:05:03",
        email:"yoga@studio.com",
        firstName:"Admin",
        id:1,
        lastName:"Admin",
        updatedAt:"2023-05-20T19:51:03"
      }
    })

    cy.get(emailInput).type("yoga@studio.com")
    cy.get(passwordInput).type(`${"test!1234"}{enter}{enter}`)
    cy.url().should('include', "/sessions")
    cy.get(".link").eq(1).click()
  })
  
})