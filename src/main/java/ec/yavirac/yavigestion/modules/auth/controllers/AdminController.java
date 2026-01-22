package ec.yavirac.yavigestion.modules.auth.controllers;

import ec.yavirac.yavigestion.modules.auth.dtos.request.role.AssignRoleDTO;
import ec.yavirac.yavigestion.modules.auth.dtos.request.role.RoleDTO;
import ec.yavirac.yavigestion.modules.auth.entities.Permission;
import ec.yavirac.yavigestion.modules.auth.entities.Role;
import ec.yavirac.yavigestion.modules.auth.services.admin.AdminService;
import ec.yavirac.yavigestion.modules.core.dtos.response.GenericOnlyTextResponse;
import ec.yavirac.yavigestion.modules.core.dtos.response.GenericPaginationResponse;
import ec.yavirac.yavigestion.modules.core.dtos.response.GenericResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin") // Ajustado para seguir el estándar v1
@Log4j2
@Tag(name = "02. Administración", description = "Gestión de roles, permisos y asignaciones de usuario")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    @Qualifier("adminServiceImpl")
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/roles")
    @Operation(summary = "Listar roles", description = "Obtiene una lista paginada de todos los roles configurados.")
    public ResponseEntity<GenericPaginationResponse<RoleDTO>> getRoles(@PageableDefault(size = 100) Pageable pageable) {
        GenericPaginationResponse<RoleDTO> response = adminService.findAllRoles(pageable);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/roles")
    @Operation(summary = "Crear rol", description = "Registra un nuevo rol en el sistema.")
    public ResponseEntity<GenericResponse<Role>> createRole(@RequestBody Role role) {
        log.info("Creando rol: {}", role.toString());
        GenericResponse<Role> createRole = adminService.createRole(role);
        return ResponseEntity.status(createRole.getStatus()).body(createRole);
    }

    @PostMapping("/permissions")
    @Operation(summary = "Crear permiso", description = "Registra un nuevo permiso atómico en el sistema.")
    public ResponseEntity<?> createPermission(@RequestBody Permission permission) {
        log.info("Creando permiso: {}", permission.toString());
        GenericResponse<Permission> createdPermission = adminService.createPermission(permission);
        return ResponseEntity.status(createdPermission.getStatus()).body(createdPermission);
    }

    @PostMapping("/roles/{roleName}/permissions")
    @Operation(summary = "Asignar permiso a rol", description = "Vincula un permiso existente a un rol específico.")
    public ResponseEntity<GenericOnlyTextResponse> addPermissionToRole(@PathVariable String roleName, @RequestParam String permissionName) {
        log.info("Asignando permiso {} al rol {}", permissionName, roleName);
        GenericOnlyTextResponse response = adminService.addPermissionToRole(roleName, permissionName);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/users/{userId}/roles")
    @Operation(summary = "Asignar rol a usuario", description = "Otorga un rol específico a un usuario mediante su ID.")
    public ResponseEntity<GenericOnlyTextResponse> addRoleToUser(@PathVariable Long userId, @RequestBody AssignRoleDTO roleName) {
        log.info("Asignando rol {} al usuario {}", roleName, userId);
        GenericOnlyTextResponse response = adminService.addRoleToUser(userId, roleName);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}