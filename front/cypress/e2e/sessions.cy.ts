describe('Session with admin credential', () => {
    const adminEmail = 'yoga@studio.com';
    const adminPassword = 'test!1234';
    const adminFirstName = 'firstName';
    const adminLastName = 'lastName';
  
    it('should login as admin', () => {
      cy.visit('/login');
  
      cy.intercept('POST', '/api/auth/login', {
        body: {
          id: 1,
          username: adminEmail,
          firstName: adminFirstName,
          lastName: adminLastName,
          admin: true
        },
      });
  
      cy.intercept('GET', '/api/session', []).as('session');
  
      cy.get('input[formControlName=email]').type(adminEmail);
      cy.get('input[formControlName=password]').type(`${adminPassword}{enter}{enter}`);
  
      cy.url().should('include', '/sessions');
    })
  
    it('should create a session', () => {
      cy.url().should('include', '/sessions');
  
      cy.intercept('GET', '/api/teacher', { body: [{ id: 1, firstName: 'Margot', lastName: 'DELAHAYE' }] });
  
      const newSession = {
        id: 1,
        name: 'Hard Workout',
        description: 'New session for expert!',
        date: '2023-05-15T00:00:00.000+00:00',
        teacher_id: 1,
      };
  
      cy.intercept('POST', '/api/session', { body: newSession });
  
      cy.intercept('GET', '/api/session', { body: [newSession] });
  
      cy.url().should('include', '/sessions');
      cy.get('button[routerLink=create]').click();
      cy.get('input[formControlName=name]').type(newSession.name);
      cy.get('input[formControlName=date]').type(newSession.date.slice(0, 10));
      cy.get('mat-select[formControlName=teacher_id]').click().get('mat-option').contains('Margot DELAHAYE').click();
      cy.get('textarea[formControlName=description]').type(newSession.description);
      cy.get('button[type=submit]').click();
      cy.url().should('include', '/sessions');
      cy.get('button[data-test-id=detail]').should('be.visible');
      cy.get('button[data-test-id=edit]').should('be.visible');
    })
  
    it('should update a session', () => {
      cy.url().should('include', '/sessions');
  
      cy.intercept('GET', '/api/teacher', {
        body: [{
          id: 1,
          firstName: 'Margot',
          lastName: 'DELAHAYE',
        }]
      });
  
      const updatedSession = {
        id: 1,
        name: 'Beginner Workout',
        description: 'New session for noobs!',
        date: '2023-05-15T00:00:00.000+00:00',
        teacher_id: 1,
        users: []
      };
  
      cy.intercept('GET', '/api/session/1', { body: updatedSession });
  
      cy.intercept('PUT', '/api/session/1', { body: updatedSession });
  
      cy.intercept('GET', '/api/session', { body: [updatedSession] });
  
      cy.get('button[data-test-id=edit]').click();
      cy.url().should('include', '/sessions/update');
      cy.get('h1').invoke('text').should('contains', 'Update session');
      cy.get('input[formControlName=name]').clear().type(updatedSession.name);
      cy.get('input[formControlName=date]').type(updatedSession.date.slice(0, 10));
      cy.get('mat-select[formControlName=teacher_id]').click().get('mat-option').contains('Margot DELAHAYE').click();
      cy.get('textarea[formControlName=description]').clear().type(updatedSession.description);
      cy.get('button[type=submit]').click();
      cy.url().should('include', '/sessions');
    })
  
    it('should delete a session', () => {
      cy.intercept('GET', '/api/teacher/1', { body: [{ id: 1, firstName: 'Margot', lastName: 'DELAHAYE' }] });
  
      const sessionToDelete = {
        id: 1,
        name: 'Beginner Workout',
        description: 'New session for noobs!',
        date: '2023-05-15T00:00:00.000+00:00',
        teacher_id: 1,
        users: []
      };
  
      cy.intercept('GET', '/api/session/1', { body: sessionToDelete });
  
      cy.intercept('GET', '/api/session', {});
  
      cy.intercept('DELETE', '/api/session/1', { statusCode: 200 });
  
      cy.url().should('include', '/sessions');
      cy.get('button[data-test-id=detail]').click();
      cy.url().should('include', '/sessions/detail');
      cy.get('h1').invoke('text').should('contains', sessionToDelete.name);
      cy.get('button[data-test-id=delete-btn]').should('be.visible');
      cy.get('button[data-test-id=delete-btn]').click();
      cy.url().should('include', '/sessions');
    })
  });
  
  describe('Session with user credential', () => {
    const userEmail = 'user@basic.com';
    const userPassword = 'user';
  
    it('should login as user', () => {
      cy.visit('/login');
  
      cy.intercept('POST', '/api/auth/login', {
        body: {
          id: 1,
          username: userEmail,
          firstName: 'firstName',
          lastName: 'lastName',
          admin: false
        },
      });
  
      cy.intercept('GET', '/api/session', {
        body: [{
          id: 1,
          name: 'Hard Workout',
          description: 'New session for expert!',
          date: '2023-05-15T00:00:00.000+00:00',
          teacher_id: 1,
          users: []
        }]
      });
  
      cy.get('input[formControlName=email]').type(userEmail);
      cy.get('input[formControlName=password]').type(`${userPassword}{enter}{enter}`);
  
      cy.url().should('include', '/sessions');
    })
  
    it('should access to session and participate and unparticipate', () => {
      cy.intercept('GET', '/api/teacher/1', { body: [{ id: 1, firstName: 'Margot', lastName: 'DELAHAYE' }] });
  
      const session = {
        id: 1,
        name: 'Beginner Workout',
        description: 'New session for noobs!',
        date: '2023-05-15T00:00:00.000+00:00',
        teacher_id: 1,
        users: []
      };
  
      cy.intercept('GET', '/api/session/1', { body: session });
  
      cy.intercept('POST', '/api/session/1/participate/1', {});
  
      cy.url().should('include', '/sessions');
      cy.get('button[data-test-id=detail]').click();
      cy.url().should('include', '/sessions/detail');
      cy.get('button[data-test-id=participate-btn]').should('exist');
      cy.get('button[data-test-id=unparticipate-btn]').should('not.exist');
  
      cy.intercept('GET', '/api/session/1', { body: { ...session, users: [1] } });
      cy.get('button[data-test-id=participate-btn]').click();
      cy.get('button[data-test-id=participate-btn]').should('not.exist');
      cy.get('button[data-test-id=unparticipate-btn]').should('exist');
  
      cy.intercept('DELETE', '/api/session/1/participate/1', {});
  
      cy.intercept('GET', '/api/session/1', { body: session });
      cy.get('button[data-test-id=unparticipate-btn]').click();
      cy.get('button[data-test-id=unparticipate-btn]').should('not.exist');
      cy.get('button[data-test-id=participate-btn]').should('exist');
    })
  })
  