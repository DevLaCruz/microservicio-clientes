package com.nttdata.CustomerMs.controller;
import com.nttdata.CustomerMs.controller.CustomerController;
import com.nttdata.CustomerMs.controller.CustomerController;
import com.nttdata.CustomerMs.model.ClienteEntity;
import com.nttdata.CustomerMs.service.ClienteService;
import com.nttdata.CustomerMs.model.Cliente;
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
        ClienteEntity cliente = new ClienteEntity();
        cliente.setDni("12345678");
        cliente.setEmail("test@example.com");

        ClienteEntity clienteEntity = new ClienteEntity();
        clienteEntity.setDni("12345678");
        clienteEntity.setEmail("test@example.com");

        when(clienteService.convertirAClienteEntity(cliente)).thenReturn(clienteEntity);

        ResponseEntity<Void> response = customerController.crearCliente(cliente);

        assertEquals(201, response.getStatusCodeValue());
        verify(clienteService, times(1)).crearCliente(clienteEntity);
    }

    @Test
    void crearCliente_Nulo() {
        ResponseEntity<Void> response = customerController.crearCliente(null);
        assertEquals(400, response.getStatusCodeValue());
        verify(clienteService, never()).crearCliente(any(ClienteEntity.class));
    }

}
