# user-authentication

### Containers

- Subir os containers mongo e mongo-express:
```
docker-compose up --build
```
- Ao inicializar o container do mongodb um usuário default é criado com os valores abaixo, ele deve ser utilizados nas chamadas iniciais dos endpoints enquanto você ainda nao tem usuários cadastrados.
```
login: admin@xpto.com
senha: 123
perfil: ADMIN
```
- Você pode visualizar o conteudo do mongo usando o endereço http://localhost:8081/ no browser, a collection que estamos utilizando é a 'authentication'.

### Aplicação
- Inicializar a aplicação springboot a partir do metodo main na classe `UserAuthenticationApplication`

### Segurança
- Todos os endpoints estão restritos a usuarios logados, utilize o usuário acima como 'Basic Auth' para logar com um usuário que você criou utilize o email como login e a senha.
- O sistema não permite cadastrar mais de um usuário com o mesmo email.

### Collection
- Na pasta collection há um arquivo json com a collection do postman, você pode importar no postman e utiliza-lá para fazer as chamadas aos endpoints.



