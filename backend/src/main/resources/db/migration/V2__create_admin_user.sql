INSERT INTO users (id,
                   email,
                   password,
                   role,
                   creation_timestamp,
                   update_timestamp)
VALUES ('${admin.user.id}',
        '${admin.user.email}',
        '${admin.user.password}',
        'ADMIN',
        NOW(),
        NOW());