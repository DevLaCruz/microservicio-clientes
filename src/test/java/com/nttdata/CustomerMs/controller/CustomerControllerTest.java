package com.nttdata.CustomerMs.controller;
import com.nttdata.CustomerMs.controller.CustomerController;
import com.nttdata.CustomerMs.exception.ClienteException;
import com.nttdata.CustomerMs.model.ClienteEntity;
import com.nttdata.CustomerMs.service.ClienteService;
import com.nttdata.customerms.model.Cliente;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerControllerTest {

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private CustomerController customerController;

    @Test
    void crearCliente() {
        Cliente cliente = new Cliente();
        cliente.setDni("12345678");
        cliente.setEmail("test@example.com");

        ClienteEntity clienteEntity = new ClienteEntity();
        clienteEntity.setDni("12345678");
        clienteEntity.setEmail("test@example.com");

        when(clienteService.convertirAClienteEntity(cliente)).thenReturn(clienteEntity);
        when(clienteService.crearCliente(clienteEntity)).thenReturn(clienteEntity);

        ResponseEntity<Void> response = customerController.crearCliente(cliente);
        assertEquals(201, response.getStatusCodeValue());
        verify(clienteService, times(1)).crearCliente(clienteEntity);
    }

    @Test
    void crearCliente_DniYaExiste() {
        Cliente cliente = new Cliente();
        cliente.setDni("12345678");

        when(clienteService.convertirAClienteEntity(cliente)).thenThrow(new ClienteException("DNI ya existe"));

        ResponseEntity<Void> response = customerController.crearCliente(cliente);
        assertEquals(400, response.getStatusCodeValue()); // Suponiendo que manejas errores 400
        verify(clienteService, times(1)).convertirAClienteEntity(cliente);
    }
}
