package com.clients.app.rest.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.clients.app.rest.Models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.clients.app.rest.exception.BadRequestException;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
public class ApiControllers {

    private static final Logger logger = LoggerFactory.getLogger(ApiControllers.class);

    @GetMapping("/api/user/info")
    public ResponseEntity<User> getUserInfo(@RequestParam String tipo, @RequestParam String numero) {
        logger.info("Recibidos parametros tipo: {} y numero: {}", tipo, numero);

        if (!isValidType(tipo)) {
            throw new BadRequestException("Tipo inválido. Debe ser 'C' o 'P'.");
        }

        return Optional.ofNullable(retrieveUser(tipo, numero))
                .map(user -> {
                    logger.debug("Usuario encontrado con tipo: {} y numero: {}", tipo, numero);
                    return new ResponseEntity<>(user, HttpStatus.OK); // Código 200
                })
                .orElseGet(() -> {
                    logger.warn("Usuario no encontrado con tipo: {} y numero: {}", tipo, numero);
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Código 404
                });
    }

    private User retrieveUser(String tipo, String numero) {
        if (isValidType(tipo) && "23445322".equals(numero)) {
            return new User("Primer Nombre", "Segundo Nombre", "Primer Apellido",
                    "Segundo Apellido", "Telefono", "Direccion", "CiudadResidencia");
        }
        return null;
    }

    private boolean isValidType(String tipo) {
        if (!"C".equals(tipo) && !"P".equals(tipo)) {
            logger.error("Tipo de parametro inválido: {}", tipo);
            return false;
        }
        return true;
    }
}
