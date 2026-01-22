package ec.yavirac.yavigestion.modules.administration.controllers;

import ec.yavirac.yavigestion.modules.administration.dtos.response.DashboardStatsDTO;
import ec.yavirac.yavigestion.modules.administration.services.facades.stats.StatsFacade;
import ec.yavirac.yavigestion.modules.core.dtos.response.GenericResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
        name = "Estadísticas",
        description = "Endpoints para consultar conteos y métricas generales del sistema"
)
@RestController
@RequestMapping("/stats")
public class StatsController {

    @Qualifier("statsFacadeImpl")
    private final StatsFacade statsFacade;

    public StatsController( StatsFacade statsFacade) {
        this.statsFacade = statsFacade;
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(
            summary = "Obtener estadísticas generales del sistema",
            description = "Retorna conteos generales como total de usuarios, carreras y periodos académicos."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta exitosa"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })

    @GetMapping("/dashboard")
    public ResponseEntity<GenericResponse<DashboardStatsDTO>> findDashboardStats() {
        GenericResponse<DashboardStatsDTO> response = statsFacade.getDashboardStats();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
