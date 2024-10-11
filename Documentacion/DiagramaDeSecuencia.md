# Diagramas de secuencia

## Cliente

### Crear Cliente

```mermaid
sequenceDiagram
    title Crear Cliente
    autonumber
    actor C as Cajero
    participant MsCliente as MicroServicio Clientes
    participant ClienteDb as Base de Datos de Clientes
    C ->> MsCliente: Solicitud para crear cliente
    activate MsCliente
    MsCliente ->> ClienteDb: Validar y almacenar cliente
    activate ClienteDb
    ClienteDb -->> MsCliente: Confirmación de almacenamiento
    deactivate ClienteDb
    MsCliente -->> C: Respuesta de éxito o error
    deactivate MsCliente
```

### Actualizar Cliente

```mermaid
sequenceDiagram
    title Actualizar Cliente
    autonumber
    actor C as Cajero
    participant MsCliente as MicroServicio Clientes
    participant ClienteDb as Base de Datos de Clientes
    C ->> MsCliente: Solicitud para actualizar cliente
    activate MsCliente
    MsCliente ->> ClienteDb: Validar y actualizar cliente
    activate ClienteDb
    ClienteDb -->> MsCliente: Confirmación de actualización
    deactivate ClienteDb
    MsCliente -->> C: Respuesta de éxito o error
    deactivate MsCliente
```

### Listar clientes

```mermaid
sequenceDiagram
    title Listar clientes
    autonumber
    actor C as Cajero
    participant MsCliente as MicroServicio Clientes
    participant ClienteDb as Base de Datos de Clientes
    C ->> MsCliente: Solicitud para listar clientes
    activate MsCliente
    MsCliente ->> ClienteDb: Consultar todo los cliente
    activate ClienteDb
    ClienteDb -->> MsCliente: Devolver lista de clientes
    deactivate ClienteDb
    MsCliente -->> C: Enviar lista de clientes
    deactivate MsCliente
```

### Detalles de Cliente

```mermaid
sequenceDiagram
    title Obtener detalles de Cliente
    autonumber
    actor C as Cajero
    participant MsCliente as MicroServicio Clientes
    participant ClienteDb as Base de Datos de Clientes
    C ->> MsCliente: Solicitud para consultar detalles de cliente
    activate MsCliente
    MsCliente ->> ClienteDb: Buscar información de cliente
    activate ClienteDb
    ClienteDb -->> MsCliente: Enviar información de cliente
    deactivate ClienteDb
    MsCliente -->> C: Respuesta con detalles de cliente
    deactivate MsCliente
```

### Eliminar Cliente

```mermaid
sequenceDiagram
    title Eliminar cliente
    autonumber
    actor C as Cajero
    participant MsCliente as MicroServicio Clientes
    participant MsCuenta as MicroServicio Cuentas
    participant ClienteDb as Base de Datos de Clientes
    participant CuentaDb as Base de Datos de Cuentas
    C ->> MsCliente: Solicitud para eliminar cliente
    activate MsCliente
    MsCliente ->> MsCuenta: Validar si cliente tiene cuenta activa
    activate MsCuenta
    MsCuenta ->> CuentaDb: Consultar si el cliente tiene cuentas activas
    activate CuentaDb
    CuentaDb -->> MsCuenta: Confirmación (cuenta activa o no)
    deactivate CuentaDb
    MsCuenta -->> MsCliente: Confirmación (cuenta activa o no)
    deactivate MsCuenta

    alt Cliente sin cuentas activas
        MsCliente ->> MsCuenta: Eliminar cuentas asociadas
        activate MsCuenta
        MsCuenta ->> CuentaDb: Eliminar toda las cuentas
        activate CuentaDb
        CuentaDb -->> MsCuenta: Confirmación de eliminación
        deactivate CuentaDb
        MsCuenta -->> MsCliente: Confirmación
        deactivate MsCuenta
        MsCliente ->> ClienteDb: Eliminar cliente
        activate ClienteDb
        ClienteDb -->> MsCliente: Confirmación de elimnación
        deactivate ClienteDb
        MsCliente -->> C: Respuesta de éxito
    else Cliente con cuentas activas
        MsCliente -->> C: Error: Cliente tiene cuentas activas
        deactivate MsCliente
    end
```

## Cuentas

### Crear cuenta

```mermaid
sequenceDiagram
    title Crear Cuenta
    autonumber
    actor C as Cajero
    participant MsCuenta as MicroServicio Cuentas
    participant MsCliente as MicroServicio Clientes
    participant ClienteDb as Base de Datos de Clientes
    participant CuentaDb as Base de Datos de Cuentas
    C ->> MsCuenta: Solicitud para crear cuenta
    activate MsCuenta
    MsCuenta ->> MsCliente: Revisar si es cliente
    activate MsCliente
    MsCliente ->> ClienteDb: Buscar cliente en base de datos
    activate ClienteDb
    ClienteDb -->> MsCliente: Cliente encontrado (o no)
    deactivate ClienteDb
    MsCliente -->> MsCuenta: Respuesta cliente encontrado (o no)
    deactivate MsCliente
    alt Cliente
        MsCuenta ->> CuentaDb: Crear nueva cuenta
        activate CuentaDb
        CuentaDb -->> MsCuenta: Confirmación
        deactivate CuentaDb
        MsCuenta -->> C: Respuesta de éxito
    else Persona no es cliente
        MsCuenta -->> C: Error: No es cliente
        deactivate MsCuenta
    end
```

### Actualizar cuenta

```mermaid
sequenceDiagram
    title Actualizar cuenta
    autonumber
    actor C as Cajero
    participant MsCuenta as MicroServicio Cuentas
    participant MsCliente as MicroServicio Clientes
    participant ClienteDb as Base de Datos de Clientes
    participant CuentaDb as Base de Datos de Cuentas
    C ->> MsCuenta: Solicitud para crear cuenta
    activate MsCuenta
    MsCuenta ->> MsCliente: Revisar si es cliente
    activate MsCliente
    MsCliente ->> ClienteDb: Buscar cliente en base de datos
    activate ClienteDb
    ClienteDb -->> MsCliente: Cliente encontrado (o no)
    deactivate ClienteDb
    MsCliente -->> MsCuenta: Respuesta cliente encontrado (o no)
    deactivate MsCliente
    alt Cliente
        MsCuenta ->> CuentaDb: Validar y actualizar cuenta
        activate CuentaDb
        CuentaDb -->> MsCuenta: Confirmación
        deactivate CuentaDb
        MsCuenta -->> C: Respuesta de éxito
    else Persona no es cliente
        MsCuenta -->> C: Error: No es cliente
        deactivate MsCuenta
    end
```

### Listar cuentas

```mermaid
sequenceDiagram
    title Listar cuentas
    autonumber
    actor C as Cajero
    participant MsCuenta as MicroServicio Cuentas
    participant CuentaDb as Base de Datos de Cuentas
    C ->> MsCuenta: Solicitud para listar cuentas
    activate MsCuenta
    MsCuenta ->> CuentaDb: Consultar todas las cuentas
    activate CuentaDb
    CuentaDb -->> MsCuenta: Devolver lista de cuentas
    deactivate CuentaDb
    MsCuenta -->> C: Enviar lista de cuentas
    deactivate MsCuenta
 ```

### Obtener detalle de cuenta

```mermaid
sequenceDiagram
    title Obtener detalles de Cuenta
    autonumber
    actor C as Cajero
    participant MsCuenta as MicroServicio Cuentas
    participant CuentaDb as Base de Datos de Cuentas
    C ->> MsCuenta: Solicitud para consultar detalle de cuentas
    activate MsCuenta
    MsCuenta ->> CuentaDb: Consultar todas las cuentas
    activate CuentaDb
    CuentaDb -->> MsCuenta: Enviar información de cuenta
    deactivate CuentaDb
    MsCuenta -->> C: Respuesta con detalle de cuenta
    deactivate MsCuenta
 ```

### Borrar cuenta

```mermaid
sequenceDiagram
    title Borrar cuenta
    autonumber
    actor C as Cajero
    participant MsCuenta as MicroServicio Cuentas
    participant CuentaDb as Base de Datos de Cuentas
    C ->> MsCuenta: Solicitud para borrar cuenta
    activate MsCuenta
    MsCuenta ->> CuentaDb: Obtener saldo actual
    activate CuentaDb
    CuentaDb -->> MsCuenta: Enviar saldo actual
    deactivate CuentaDb

    alt Saldo en 0
        MsCuenta ->> CuentaDb: Solicitar eliminación de cuenta
        activate CuentaDb
        CuentaDb -->> MsCuenta: Confirmación de eliminación exitosa
        deactivate CuentaDb
        MsCuenta -->> C: Confirmación: Cuenta eliminada exitosamente
    else Saldo es negativo
        MsCuenta -->> C: Error: Cuenta sobregirada (Realizar pago antes de eliminar)
    else Saldo positivo
        MsCuenta -->> C: Error: Retirar saldo restantes antes de eliminar
        deactivate MsCuenta
    end
 ```

### Depositar

```mermaid
sequenceDiagram
    title Hacer depósito en Cuenta
    autonumber
    actor C as Cajero
    participant MsCuenta as MicroServicio Cuentas
    participant CuentaDb as Base de Datos de Cuentas
    participant MsTransaction as Microservicio Transacciones
    C ->> MsCuenta: Solicitud para hacer depósito
    activate MsCuenta
    MsCuenta ->> CuentaDb: Verificar existencia de la cuenta
    activate CuentaDb
    CuentaDb -->> MsCuenta: Cuenta valida
    deactivate CuentaDb
    MsCuenta ->> CuentaDb: Actualizar saldo con monto depósitado
    activate CuentaDb
    CuentaDb -->> MsCuenta: Confirmación de saldo actualizado
    deactivate CuentaDb
    activate MsTransaction
    MsCuenta ->> MsTransaction: Registrar transacción
    MsTransaction -->> MsCuenta: Confirmación de registro
    deactivate MsTransaction
    MsCuenta ->> C: Depósito realizado
    deactivate MsCuenta
 ```

### Retirar

```mermaid
sequenceDiagram
    title Hacer Retiro en Cuenta
    autonumber
    actor C as Cajero
    participant MsCuenta as MicroServicio Cuentas
    participant CuentaDb as Base de Datos de Cuentas
    participant MsTransaction as Microservicio Transacciones
    C ->> MsCuenta: Solicitud para hacer retiro
    activate MsCuenta
    MsCuenta ->> CuentaDb: Obtener saldo actual
    activate CuentaDb
    CuentaDb -->> MsCuenta: Enviar saldo actual
    deactivate CuentaDb

    alt Saldo en adecuado
        MsCuenta ->> CuentaDb: Actualizar saldo con monto retirado
        activate CuentaDb
        CuentaDb -->> MsCuenta: Confirmación de saldo actualizado
        deactivate CuentaDb
        activate MsTransaction
        MsCuenta ->> MsTransaction: Registrar transacción
        MsTransaction -->> MsCuenta: Confirmación de registro
        deactivate MsTransaction
        MsCuenta -->> C: Confirmación: Retiro realizado exitosamente
    else Saldo en insuficiente: -500 (corriente) o 0 (ahorro)
        MsCuenta -->> C: Error: Saldo insuficiente
        deactivate MsCuenta
    end
 ```

### Transferir de una cuenta otra

```mermaid
sequenceDiagram
    title Hacer Transferencia de una cuenta a otra
    autonumber
    actor C as Cajero
    participant MsCuenta as MicroServicio Cuentas
    participant CuentaDb as Base de Datos de Cuentas
    participant MsTransaction as Microservicio Transacciones
    C ->> MsCuenta: Solicitud para hacer transferencia
    activate MsCuenta
    MsCuenta ->> CuentaDb: Solicitar información de las cuentas
    activate CuentaDb
    CuentaDb -->> MsCuenta: Enviar información de cuentas
    deactivate CuentaDb

    alt Saldo en adecuado y cuentas validas
        MsCuenta ->> CuentaDb: Actualizar saldos de cuentas
        activate CuentaDb
        CuentaDb -->> MsCuenta: Confirmación de saldos actualizados
        deactivate CuentaDb
        activate MsTransaction
        MsCuenta ->> MsTransaction: Registrar transacción
        MsTransaction -->> MsCuenta: Confirmación de registro
        deactivate MsTransaction
        MsCuenta -->> C: Confirmación: Transacción realizada exitosamente
    else Saldo en insuficiente: -500 (corriente) o 0 (ahorro)
        MsCuenta -->> C: Error: Saldo insuficiente
        deactivate MsCuenta
    end
 ```

## Transacciones

### Registrar un depósito

```mermaid
sequenceDiagram
    title Registrar un depósito
    autonumber
    participant MsCuenta as MicroServicio Cuentas
    participant MsTransaction as MicroServicio Transacciones
    participant TransactionDb as Base de Datos NoSQL de transacciones
    activate MsTransaction
    MsCuenta ->> MsTransaction: Solicitud para registrar depósito
    activate TransactionDb
    MsTransaction ->> TransactionDb: Registrar depósito
    TransactionDb -->> MsTransaction: Confirmación de almacenamiento
    deactivate TransactionDb
    MsTransaction -->> MsCuenta: Respuesta de éxito o error
    deactivate MsTransaction
```

### Registrar un retiro

```mermaid
sequenceDiagram
    title Registrar un retiro
    autonumber
    participant MsCuenta as MicroServicio Cuentas
    participant MsTransaction as MicroServicio Transacciones
    participant TransactionDb as Base de Datos NoSQL de transacciones
    activate MsTransaction
    MsCuenta ->> MsTransaction: Solicitud para registrar retiro
    activate TransactionDb
    MsTransaction ->> TransactionDb: Registrar retiro
    TransactionDb -->> MsTransaction: Confirmación de almacenamiento
    deactivate TransactionDb
    MsTransaction -->> MsCuenta: Respuesta de éxito o error
    deactivate MsTransaction
```

### Registrar una transferencia

```mermaid
sequenceDiagram
    title Registrar un transferencia
    autonumber
    participant MsCuenta as MicroServicio Cuentas
    participant MsTransaction as MicroServicio Transacciones
    participant TransactionDb as Base de Datos NoSQL de transacciones
    activate MsTransaction
    MsCuenta ->> MsTransaction: Solicitud para registrar transferencia
    activate TransactionDb
    MsTransaction ->> TransactionDb: Registrar transferencia
    TransactionDb -->> MsTransaction: Confirmación de transferencia
    deactivate TransactionDb
    MsTransaction -->> MsCuenta: Respuesta de éxito o error
    deactivate MsTransaction
```
### Obtener historial de transacciones


```mermaid
sequenceDiagram
    title Obtener historial de transacciones
    autonumber
    participant MsCuenta as MicroServicio Cuentas
    participant MsTransaction as MicroServicio Transacciones
    participant TransactionDb as Base de Datos NoSQL de transacciones
    activate MsTransaction
    MsCuenta ->> MsTransaction: Solicitud para obtener historial de transferencias
    activate TransactionDb
    MsTransaction ->> TransactionDb: Obtener transferencias
    TransactionDb -->> MsTransaction: Enviar registros de transferencias
    deactivate TransactionDb
    MsTransaction -->> MsCuenta: Respuesta de éxito o error
    deactivate MsTransaction
```
