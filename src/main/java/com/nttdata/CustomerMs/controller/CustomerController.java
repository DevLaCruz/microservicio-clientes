package com.nttdata.CustomerMs.controller;

import com.nttdata.customerms.api.CustomersApiDelegate;
import com.nttdata.CustomerMs.model.ClienteEntity;
import com.nttdata.CustomerMs.service.ClienteService;
import com.nttdata.customerms.model.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("clientes")
public class CustomerController implements CustomersApiDelegate {

    @Autowired
    private ClienteService clienteService;

    @Override
    @PostMapping
    public ResponseEntity<Void> crearCliente(@Valid @RequestBody Cliente cliente) {
        // Crear cliente y no devolver el objeto, solo el estado
        clienteService.crearCliente(clienteService.convertirAClienteEntity(cliente));
        return ResponseEntity.status(201).build();
    }

    @Override
    @GetMapping
    public ResponseEntity<List<Cliente>> listarClientes() {
        List<ClienteEntity> clientesEntities = clienteService.listarClientes();
        List<Cliente> clientes = clientesEntities.stream()
                .map(clienteService::convertirACliente)
                .collect(Collectors.toList());
        return ResponseEntity.ok(clientes);
    }


    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtenerCliente(@PathVariable Integer id) {
        ClienteEntity clienteEntity = clienteService.obtenerCliente(id);
        return ResponseEntity.ok(clienteService.convertirACliente(clienteEntity));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizarCliente(@PathVariable Integer id, @Valid @RequestBody Cliente cliente) {
        clienteService.actualizarCliente(id, clienteService.convertirAClienteEntity(cliente));
        return ResponseEntity.ok().build();
    }

    // Método para eliminar un cliente por ID
    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Integer id) {
        clienteService.eliminarCliente(id);
        return ResponseEntity.noContent().build();  // Devuelve un 204 No Content
    }
}
