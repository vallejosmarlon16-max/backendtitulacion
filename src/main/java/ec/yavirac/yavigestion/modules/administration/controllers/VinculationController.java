package ec.yavirac.yavigestion.modules.administration.controllers;

import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import ec.yavirac.yavigestion.modules.administration.entities.Project;
import ec.yavirac.yavigestion.modules.core.dtos.response.GenericPaginationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ec.yavirac.yavigestion.modules.administration.entities.Vinculation;
import ec.yavirac.yavigestion.modules.administration.services.database.vinculation.VinculationService;

@RestController
@RequestMapping("/api/v1/vinculation") // Agregamos /api/v1 para mantener el estándar del proyecto
@CrossOrigin("*")
@Tag(name = "01. Vinculación", description = "Endpoints para la gestión de prácticas pre-profesionales")
@SecurityRequirement(name = "bearerAuth")
public class VinculationController {

    @Autowired
    private VinculationService service;

    @PostMapping
    @Operation(summary = "Crear registro", description = "Permite crear una nueva vinculación en la base de datos.")
    public Vinculation create(@RequestBody Vinculation vinculation) {
        return service.save(vinculation);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener por ID", description = "Busca una vinculación específica mediante su identificador único.")
    public Vinculation getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping
    @Operation(summary = "Listar todas", description = "Retorna una lista paginada de todas las vinculaciones registradas.")
    public GenericPaginationResponse<Vinculation> getAll(@PageableDefault(size = 15) Pageable pageable) {
        Page<Vinculation> page = service.findAll(pageable);
        return ResponseEntity.ok(GenericPaginationResponse
                .<Vinculation>builder()
                .currentPage(pageable.getPageNumber())
                .data(page.getContent())
                .totalPages(page.getTotalPages())
                .pageSize(pageable.getPageSize())
                .totalElements(page.getTotalElements())
                .status(200)
                .build()).getBody();
    }

    @PutMapping
    @Operation(summary = "Actualizar registro", description = "Actualiza la información de una vinculación existente.")
    public Vinculation update(@RequestBody Vinculation vinculation) {
        return service.update(vinculation);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar registro", description = "Elimina de forma permanente una vinculación del sistema.")
    public void delete(@PathVariable Long id) {
        service.deleteById(id);
    }
}