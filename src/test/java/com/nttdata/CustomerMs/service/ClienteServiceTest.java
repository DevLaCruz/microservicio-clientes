package com.nttdata.CustomerMs.service;
import com.nttdata.CustomerMs.exception.ClienteException;
import com.nttdata.CustomerMs.model.ClienteEntity;
import com.nttdata.CustomerMs.repository.ClienteRepository;
import com.nttdata.CustomerMs.service.ClienteService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    public ClienteServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void crearCliente() {
        ClienteEntity clienteEntity = new ClienteEntity();
        clienteEntity.setDni("12345678");
        clienteEntity.setEmail("test@example.com");

        when(clienteRepository.save(clienteEntity)).thenReturn(clienteEntity);

        ClienteEntity result = clienteService.crearCliente(clienteEntity);

        assertNotNull(result);
        verify(clienteRepository, times(1)).save(clienteEntity);
    }

    @Test
    void crearCliente_DniYaExiste() {
        ClienteEntity clienteEntity = new ClienteEntity();
        clienteEntity.setDni("12345678");

        when(clienteRepository.existsByDni("12345678")).thenReturn(true);

        assertThrows(ClienteException.class, () -> clienteService.crearCliente(clienteEntity));
        verify(clienteRepository, never()).save(any(ClienteEntity.class));
    }
}
