```swagger
openapi: 3.0.3
info:
  title: XYZ Bank
  version: '1.0.0'
  description: |
    Sistema para gestionar transacciones bancarias entre accounts de customers. El sistema debe permitir la creación de customers, accounts, y realizar transacciones entre ellas (retiros y depósitos).

servers:
  - url: http://localhost:8080/
tags:
  - name: Customer
    description: Gestión de customers
  - name: Account
    description: Gestión de accounts, pagos y retiros
paths:
  /customer:
    post:
      tags:
        - Customer
      operationId: createCustomer
      summary: Crear un nuevo customer
      description: Endpoint que permite crear un customer nuevo del banco
      requestBody:
        $ref: '#/components/requestBodies/CustomerRequest'
      responses:
        '200':
          description: Operación exitosa
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/CustomerResponse'
        '400':
          $ref: "#/components/responses/BadPetition"
        "401":
          $ref: "#/components/responses/Unauthorized"
        '422':
          $ref: "#/components/responses/UnprocessableContent"
    get:
      operationId: findCustomers
      tags:
        - Customer
      summary: Listar todos los customers
      description: Endpoint que permite obtener una lista de los customers del banco
      parameters:
        - $ref: '#/components/parameters/limitParam'
        - $ref: '#/components/parameters/offsetParam'
      responses:
        '200':
          description: Operación exitosa
          content:
            application/json:
              schema:
                type: array
                items:
                  $ref: '#/components/schemas/CustomerResponse'
        '400':
          $ref: "#/components/responses/BadPetition"
        "401":
          $ref: "#/components/responses/Unauthorized"
        '404':
          $ref: "#/components/responses/NotFound"

  /customer/{id}:
    get:
      operationId: findCustomerById
      tags:
        - Customer
      summary: Obtener detalles de un customer por su ID
      description: Obtiene detalles de un customer del banco
      parameters:
        - $ref: '#/components/parameters/uuidPath'
      responses:
        '200':
          description: Operación exitosa
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/CustomerResponse'
        '400':
          $ref: "#/components/responses/BadPetition"
        "401":
          $ref: "#/components/responses/Unauthorized"
        '404':
          $ref: "#/components/responses/NotFound"
    put:
      operationId: updateCustomer
      tags:
        - Customer
      summary: Actualizar a un customer existente
      description: Endpoint que permite editar la información de un customer del banco
      parameters:
        - $ref: '#/components/parameters/uuidPath'
      requestBody:
        $ref: '#/components/requestBodies/CustomerRequest'
      responses:
        '200':
          description: Operación exitosa
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/CustomerResponse'
        '400':
          $ref: "#/components/responses/BadPetition"
        "401":
          $ref: "#/components/responses/Unauthorized"
        '404':
          $ref: "#/components/responses/NotFound"
        '422':
          $ref: "#/components/responses/UnprocessableContent"
    delete:
      operationId: deleteCustomer
      tags:
        - Customer
      summary: Borra a un Customer del banco
      description: Endpoint que permite eliminar un customer registrado en el banco
      parameters:
        - $ref: '#/components/parameters/uuidPath'
      responses:
        "200":
          description: Customer eliminado correctamente.
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ModelApiResponse'
              example:
                message: "Customer eliminado exitosamente"

        '400':
          $ref: "#/components/responses/BadPetition"
        "401":
          $ref: "#/components/responses/Unauthorized"
        '404':
          $ref: "#/components/responses/NotFound"

  /account:
    post:
      tags:
        - Account
      summary: Crear una account nueva
      description: Endpoint que permite crear una account nueva a un customer
      operationId: createAccount
      requestBody:
        $ref: '#/components/requestBodies/AccountRequest'
      responses:
        '200':
          description: Operación exitosa
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/AccountResponse'
        '400':
          $ref: "#/components/responses/BadPetition"
        "401":
          $ref: "#/components/responses/Unauthorized"
        '422':
          $ref: "#/components/responses/UnprocessableContent"
    get:
      operationId: findAccounts
      tags:
        - Account
      summary: Listar todas las accounts
      description: Endpoint que permite obtener una lista de accounts del banco
      parameters:
        - $ref: '#/components/parameters/limitParam'
        - $ref: '#/components/parameters/offsetParam'
        - $ref: '#/components/parameters/clienteIdParam'
      responses:
        '200':
          description: Operación exitosa
          content:
            application/json:
              schema:
                type: array
                items:
                  $ref: '#/components/schemas/AccountResponse'
        '400':
          $ref: "#/components/responses/BadPetition"
        "401":
          $ref: "#/components/responses/Unauthorized"
        '404':
          $ref: "#/components/responses/NotFound"

  /account/{id}:
    parameters:
      - $ref: '#/components/parameters/uuidPath'
    get:
      operationId: findAccountById
      tags:
        - Account
      summary: Obtener detalles de un account por su ID
      description: Obtiene detalles de un account del banco
      responses:
        '200':
          description: Operación exitosa
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/AccountResponse'
        '400':
          $ref: "#/components/responses/BadPetition"
        "401":
          $ref: "#/components/responses/Unauthorized"
        '404':
          $ref: "#/components/responses/NotFound"

    put:
      operationId: updateAccount
      tags:
        - Account
      summary: Actualizar a un account existente
      description: Endpoint que permite editar la información de un account del banco
      requestBody:
        $ref: '#/components/requestBodies/AccountRequest'
      responses:
        '200':
          description: Operación exitosa
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/AccountResponse'
        '400':
          $ref: "#/components/responses/BadPetition"
        "401":
          $ref: "#/components/responses/Unauthorized"
        '404':
          $ref: "#/components/responses/NotFound"
        '422':
          $ref: "#/components/responses/UnprocessableContent"
    delete:
      operationId: deleteAccount
      tags:
        - Account
      summary: Borra a una account del usuario
      description: Endpoint que permite eliminar un account registrado en el banco
      responses:
        "200":
          description: Account eliminada correctamente.
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ModelApiResponse'
              example:
                message: "Account eliminada exitosamente"
        '400':
          $ref: "#/components/responses/BadPetition"
        "401":
          $ref: "#/components/responses/Unauthorized"
        '404':
          $ref: "#/components/responses/NotFound"

  /account/{id}/depositar:
    put:
      tags:
        - Account
      summary: Depositar dinero a una account
      description: Depositar dinero a una account según el id
      operationId: depositToAccount
      parameters:
        - $ref: '#/components/parameters/uuidPath'
      requestBody:

        $ref: '#/components/requestBodies/MoneyRequest'
      responses:
        "200":
          description: Account eliminada correctamente.
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ModelApiResponse'
              example:
                message: "Deposito de S/500 realizado"
        '400':
          $ref: "#/components/responses/BadPetition"
        "401":
          $ref: "#/components/responses/Unauthorized"
        '404':
          $ref: "#/components/responses/NotFound"
        '422':
          $ref: "#/components/responses/UnprocessableContent"
  /account/{id}/retirar:
    put:
      operationId: withdrawFromAccount
      tags:
        - Account
      summary: Depositar dinero a una account
      description: Retirar dinero de una account
      parameters:
        - $ref: '#/components/parameters/uuidPath'
      requestBody:
        $ref: '#/components/requestBodies/MoneyRequest'
      responses:
        "200":
          description: Account eliminada correctamente.
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ModelApiResponse'
              example:
                message: "Retiro de S/500 realizado"
        '400':
          $ref: "#/components/responses/BadPetition"
        "401":
          $ref: "#/components/responses/Unauthorized"
        '404':
          $ref: "#/components/responses/NotFound"
        '422':
          $ref: "#/components/responses/UnprocessableContent"
components:
  responses:
    BadPetition:
      description: Mala petición
      content:
        application/json:
          schema:
            $ref: "#/components/schemas/Error"
    NotFound:
      description: El recurso especifico no fue encontrado
      content:
        application/json:
          schema:
            $ref: "#/components/schemas/Error"
    Unauthorized:
      description: No autorizado
      content:
        application/json:
          schema:
            $ref: "#/components/schemas/Error"
    UnprocessableContent:
      description: Error de validación
      content:
        application/json:
          schema:
            $ref: "#/components/schemas/Error"

  parameters:
    uuidPath:
      in: path
      name: id
      description: ID del recurso a procesar
      required: true
      schema:
        type: string
        format: uuid
    offsetParam:
      in: query
      name: offset
      required: false
      schema:
        type: integer
        minimum: 0
        default: 0
      description: Desde cual registro se desea consultar. Por ejemplo para consultar desde el registro 20, se envía offset=20
    clienteIdParam:
      in: query
      name: clienteId
      description: ID de cliente a buscar
      required: false
      schema:
        type: string
        format: uuid
    limitParam:
      in: query
      name: limit
      required: false
      schema:
        type: integer
        maximum: 100
        default: 20
      description: Cantidad de registros a retornar por cada consulta
  schemas:
    CustomerResponse:
      required:
        - dni
        - nombre
        - apellido
        - email
      type: object
      properties:
        id:
          type: string
          format: uuid
          description: Id del customer.
          example: 123e4567-e89b-12d3-a456-426655440000
          readOnly: true
        nombre:
          type: string
          minLength: 1
          maxLength: 30
          example: Danny
          description: Nombre del customer.
        apellido:
          type: string
          example: Vera Palomino
          minLength: 1
          maxLength: 60
          description: Apellido completo del customer.
        dni:
          type: string
          example: 77885544
          minLength: 8
          maxLength: 8
          description: Documento nacional de Identidad.
        email:
          type: string
          format: email
          example: danny@nttdata.com
          description: Correo del customer.
    AccountResponse:
      required:
        - id
        - numeroCuenta
        - tipoAccount
        - saldo
        - clienteId
      type: object
      properties:
        id:
          type: string
          format: uuid
          description: Id de account.
          readOnly: true
          example: 123e4567-e89b-12d3-a456-426655440000
        numeroCuenta:
          type: string
          description: Numero de account.
          readOnly: true
          example: BC1200192008
        saldo:
          type: number
          format: double
          example: 0.0
          default: 0
          minimum: -500
          description: Saldo de la account.
        tipoCuenta:
          enum:
            - Ahorro
            - Corriente
          example: Ahorro
          description: Tipo de account puede ser Ahorro o Corriente.
        clienteId:
          type: string
          format: uuid
          example: 123e4567-e89b-12d3-a456-426655440000
          description: El ID del customer propietario de la account
    CustomerRequest:
      type: object
      required:
        - nombre
        - apellido
        - email
        - dni
      properties:
        id:
          type: string
          format: uuid
          description: Id del customer.
          example: 123e4567-e89b-12d3-a456-426655440000
          readOnly: true
        nombre:
          type: string
          minLength: 1
          maxLength: 30
          example: Danny
          description: Nombre del customer.
        apellido:
          type: string
          example: Vera Palomino
          minLength: 1
          maxLength: 60
          description: Apellido completo del customer.
        dni:
          type: string
          example: 77885544
          minLength: 8
          maxLength: 8
          description: Documento nacional de Identidad.
        email:
          type: string
          format: email
          example: danny@nttdata.com
          description: Correo del customer.
    AccountRequest:
      type: object
      properties:
        id:
          type: string
          format: uuid
          description: Id de account.
          readOnly: true
          example: 123e4567-e89b-12d3-a456-426655440000
        numeroCuenta:
          type: string
          description: Numero de account.
          readOnly: true
          example: BC1200192008
        saldo:
          type: number
          format: double
          example: 0.0
          default: 0
          minimum: -500
          description: Saldo de la account.
        tipoCuenta:
          enum:
            - Ahorro
            - Corriente
          example: Ahorro
          description: Tipo de account puede ser Ahorro o Corriente.
        clienteId:
          type: string
          format: uuid
          example: 123e4567-e89b-12d3-a456-426655440000
          description: El ID del customer propietario de la account
    Error:
      type: object
      properties:
        code:
          type:  integer
        message:
          type: string
      required:
        - code
        - message
    Money:
      type: object
      properties:
        dinero:
          type: number
          format: double
          example: 100.50
          default: 0
          description: Cantidad de dinero a retirar o depositar

    ModelApiResponse:
      type: object
      properties:
        message:
          type: string
          description: Mensaje de la respuesta
  requestBodies:
    MoneyRequest:
      description: Un JSON que contiene información del dinero a depositar o retirar
      content:
        application/json:
          schema:
            $ref: '#/components/schemas/Money'
    CustomerRequest:
      description: Un JSON que contiene información de un customer
      content:
        application/json:
          schema:
            $ref: '#/components/schemas/CustomerRequest'
    AccountRequest:
      description: Un JSON que contiene información de una account
      content:
        application/json:
          schema:
            $ref: '#/components/schemas/AccountRequest'
```