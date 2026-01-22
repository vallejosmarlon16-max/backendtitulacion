package ec.yavirac.yavigestion.modules.administration.controllers;

import ec.yavirac.yavigestion.modules.administration.entities.Project;
import ec.yavirac.yavigestion.modules.auth.decorators.HasPermission;
import ec.yavirac.yavigestion.modules.core.dtos.response.GenericPaginationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

import ec.yavirac.yavigestion.modules.administration.services.database.projects.ProjectService;

@Tag(
        name = "Proyectos",
        description = "Operaciones relacionadas con la gestión de proyectos de titulación"
)
@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService service;
    @Operation(
            summary = "Crear proyecto",
            description = "Crea un nuevo proyecto de titulación."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Proyecto creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Project.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "No autenticado",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "No autorizado",
                    content = @Content)
    })
    @PostMapping
    @HasPermission("project:create")
    public ResponseEntity<Project> create(@RequestBody Project project) {
        return ResponseEntity.ok(service.save(project));
    }

    @Operation(
            summary = "Listar proyectos",
            description = "Obtiene un listado  de proyectos. "
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Listado de proyectos obtenido correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GenericPaginationResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "No autorizado", content = @Content)
    })
    @GetMapping
    @HasPermission("project:findAll")
    public ResponseEntity<GenericPaginationResponse<Project>> list(@PageableDefault(size = 15) Pageable pageable) {
        Page<Project> page = service.findAll(pageable);
        return ResponseEntity.ok(GenericPaginationResponse
                .<Project>builder()
                .currentPage(pageable.getPageNumber())
                .data(page.getContent())
                .totalPages(page.getTotalPages())
                .pageSize(pageable.getPageSize())
                .totalElements(page.getTotalElements())
                .status(200)
                .build());
    }
    @Operation(
            summary = "Obtener proyecto por ID",
            description = "Obtiene un proyecto específico según su ID. "
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Proyecto encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Project.class)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "No autorizado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Proyecto no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    @HasPermission("project:findById")
    public ResponseEntity<Project> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(
            summary = "Actualizar proyecto",
            description = "Actualiza un proyecto por su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Proyecto actualizado correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Project.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "Proyecto no encontrado")
    })
    @PutMapping("/{id}")
    @HasPermission("project:update")
    public ResponseEntity<Project> update(@PathVariable Long id, @RequestBody Project project) {
        return ResponseEntity.ok(service.update(id, project));
    }

    @Operation(
            summary = "Eliminar proyecto",
            description = "Elimina un proyecto por su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Proyecto eliminado correctamente"
            ),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "Proyecto no encontrado")
    })
    @DeleteMapping("/{id}")
    @HasPermission("project:delete")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
