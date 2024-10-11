package com.nttdata.CustomerMs.service;

import com.nttdata.CustomerMs.exception.ClienteException;
import com.nttdata.CustomerMs.model.ClienteEntity;
import com.nttdata.CustomerMs.repository.ClienteRepository;
import com.nttdata.CustomerMs.model.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired

    private final ClienteRepository clienteRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository, RestTemplate restTemplate) {
        this.clienteRepository = clienteRepository;
        this.restTemplate = restTemplate;
    }

    public ClienteEntity crearCliente(ClienteEntity clienteEntity) {
        validarCliente(clienteEntity); // Llamar al método de validación
        return clienteRepository.save(clienteEntity);
    }

    // Método para validar DNI y email
    private void validarCliente(ClienteEntity clienteEntity) {
        if (!esEmailValido(clienteEntity.getEmail())) {
            throw new ClienteException("Email no válido");
        }
        if (clienteRepository.existsByDni(clienteEntity.getDni())) {
            throw new ClienteException("DNI ya existe");
        }
    }


    // Método para validar el formato de email
    private boolean esEmailValido(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email != null && email.matches(emailRegex);
    }

    public List<ClienteEntity> listarClientes() {
        return clienteRepository.findAll();
    }

    public ClienteEntity obtenerCliente(Integer id) {
        return clienteRepository.findById(id).orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    }

    public void actualizarCliente(Integer id, ClienteEntity clienteEntity) {
        ClienteEntity clienteExistente = obtenerCliente(id);
        clienteExistente.setNombre(clienteEntity.getNombre());
        clienteExistente.setApellido(clienteEntity.getApellido());
        clienteExistente.setDni(clienteEntity.getDni());
        clienteExistente.setEmail(clienteEntity.getEmail());
        clienteRepository.save(clienteExistente);
    }

    public void eliminarCliente(Integer id) {
        ClienteEntity cliente = obtenerCliente(id);

        // Llamar al endpoint que devuelve todas las cuentas
        String url = "http://localhost:8081/cuentas"; // Ajusta según tu API
        Map[] cuentas = restTemplate.getForObject(url, Map[].class); // Usando Map para no definir una clase

        // Verificar si el clienteId está presente en alguna cuenta
        boolean tieneCuentasActivas = Arrays.stream(cuentas)
                .anyMatch(cuenta -> cliente.getId().equals(cuenta.get("clienteId")));

        if (tieneCuentasActivas) {
            throw new ClienteException("El cliente tiene cuentas activas y no se puede eliminar");
        }

        clienteRepository.delete(cliente);
    }

    // Método para convertir Cliente a ClienteEntity
    public ClienteEntity convertirAClienteEntity(Cliente cliente) {
        ClienteEntity clienteEntity = new ClienteEntity();
        clienteEntity.setId(cliente.getId());
        clienteEntity.setNombre(cliente.getNombre());
        clienteEntity.setApellido(cliente.getApellido());
        clienteEntity.setDni(cliente.getDni());
        clienteEntity.setEmail(cliente.getEmail());
        return clienteEntity;
    }


    // Método para convertir ClienteEntity a Cliente
    public Cliente convertirACliente(ClienteEntity clienteEntity) {
        Cliente cliente = new Cliente();
        cliente.setId(clienteEntity.getId());
        cliente.setNombre(clienteEntity.getNombre());
        cliente.setApellido(clienteEntity.getApellido());
        cliente.setDni(clienteEntity.getDni());
        cliente.setEmail(clienteEntity.getEmail());
        return cliente;
    }

}
