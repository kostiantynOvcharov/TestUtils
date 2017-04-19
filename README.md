Using utils classes from package /testutils on the basis of reflection, you can create such automated tests for example

@Test
   public void class_CreateUserRequest_must_be_dto()
   {
      IValueProvider<PortalSecurityUserId> id =
            new ValueProvider<PortalSecurityUserId>(PortalSecurityUserId.class, new PortalSecurityUserId("id1"),
            new PortalSecurityUserId("id2"));
      IValueProvider<String> login = new ValueProvider<String>(String.class, "login1", "login2");
      IValueProvider<String> password = new ValueProvider<String>(String.class, "password1", "password2");
      IValueProvider<Boolean> useExternalUserPasswordRules = new ValueProvider<Boolean>(boolean.class, true, false);
      IValueProvider<Iterable> accessRoles = new ValueProvider<Iterable>(
            Iterable.class,
            ImmutableList.of(new AccessRoleId("roleId1")),
            ImmutableList.of(new AccessRoleId("roleId2")));
      IValueProvider<MailAddress> email =
            new ValueProvider<MailAddress>(MailAddress.class, new MailAddress("email1@email.com"), new MailAddress("email2@email.com"));
      IValueProvider<Boolean> emailConfirmed = new ValueProvider<Boolean>(boolean.class, true, false);
      IValueProvider<String> lastName = new ValueProvider<String>(String.class, "lastName1", "lastName2");
      IValueProvider<String> firstName = new ValueProvider<String>(String.class, "firstName1", "firstName2");
      IValueProvider<String> middleName = new ValueProvider<String>(String.class, "middleName1", "middleName2");

      Asserts.assertThatClass(CreatePortalSecurityUserRequest.class)
            .withConstructor()
               .withArg(id, "id")
               .withArg(login, "login")
               .withArg(password, "password")
               .withArg(useExternalUserPasswordRules, "useExternalUserPasswordRules")
               .withArg(accessRoles, "accessRoles")
               .withArg(email, "email")
               .withArg(emailConfirmed, "emailConfirmed")
               .withArg(lastName, "lastName")
               .withArg(firstName, "firstName")
               .withArg(middleName, "middleName")
            .endMethod()
            .hasCorrectInitialization()
            .protectedAgainstNullArguments()
            .isImmutable()
            .finishAssert();
   }
