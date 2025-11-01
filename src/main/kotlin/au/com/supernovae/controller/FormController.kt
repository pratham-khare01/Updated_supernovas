package au.com.supernovae.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.beans.factory.annotation.Autowired
import au.com.supernovae.service.FormService
import java.time.OffsetDateTime
import java.util.*

data class TelecomFormDTO(
    val fullname: String?,
    val company: String?,
    val email: String?,
    val phone: String?,
    val projectType: String?,
    val message: String?,
    val preferredContact: String?,
    val consent: Boolean? = false
)

data class ChargingPartnerDTO(
    val fullname: String?,
    val company: String?,
    val role: String?,
    val email: String?,
    val phone: String?,
    val locations: String?,
    val chargerTypes: List<String> = emptyList(),
    val description: String?,
    val consent: Boolean? = false
)

@RestController
@RequestMapping("/api/forms")
class FormController @Autowired constructor(
    private val formService: FormService
) {

    @PostMapping("/telecom-consultation")
    fun submitTelecom(@RequestBody dto: TelecomFormDTO): ResponseEntity<String> {
        if (dto.fullname.isNullOrBlank() || dto.email.isNullOrBlank()) {
            return ResponseEntity.badRequest().body("fullname and email required")
        }
        formService.saveForm("telecom_consultation", dto)
        return ResponseEntity.ok("submitted")
    }

    @PostMapping("/become-charging-partner")
    fun submitChargingPartner(@RequestBody dto: ChargingPartnerDTO): ResponseEntity<String> {
        if (dto.fullname.isNullOrBlank() || dto.company.isNullOrBlank() || dto.email.isNullOrBlank()) {
            return ResponseEntity.badRequest().body("fullname, company and email required")
        }
        formService.saveForm("become_charging_partner", dto)
        return ResponseEntity.ok("submitted")
    }
}
