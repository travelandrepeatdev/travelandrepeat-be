package com.travelandrepeat.api.service;

import com.travelandrepeat.api.dto.QuotationFormRequest;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@SuppressWarnings("unused")
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MimeMessage mimeMessage;

    @Override
    public void sendMail(QuotationFormRequest request) {
        String mailContent = """
                <!DOCTYPE html>
                <html lang="es">
                <head>
                  <meta charset="UTF-8">
                  <title>Solicitud de Cotización</title>
                </head>
                <body style="margin:0; padding:0; background-color:#f4f6f8; font-family: Arial, Helvetica, sans-serif;">
                
                <table width="100%" cellpadding="0" cellspacing="0" style="background-color:#f4f6f8; padding:20px;">
                  <tr>
                    <td align="center">
                
                      <!-- Contenedor -->
                      <table width="600" cellpadding="0" cellspacing="0" style="background-color:#ffffff; border-radius:8px; overflow:hidden; box-shadow:0 2px 6px rgba(0,0,0,0.08);">
                
                        <!-- Header -->
                        <tr>
                          <td style="background-color:#1e293b; padding:20px; text-align:center; color:#ffffff;">
                            <h1 style="margin:0; font-size:22px;">Nueva Solicitud de Cotización</h1>
                            <p style="margin:5px 0 0; font-size:14px; opacity:0.9;">
                              Detalles enviados desde el formulario web
                            </p>
                          </td>
                        </tr>
                
                        <!-- Content -->
                        <tr>
                          <td style="padding:25px; color:#334155; font-size:14px;">
                
                            <!-- CLIENT INFO -->
                            <h3 style="margin-top:0; color:#0f172a; border-bottom:1px solid #e5e7eb; padding-bottom:6px;">
                              Información del Cliente
                            </h3>
                            <p>
                              <b>Nombre:</b> {{completeName}}<br>
                              <b>Email:</b> {{email}}<br>
                              <b>Teléfono:</b> {{phone}}<br>
                              <b>País / Ciudad:</b> {{countryCity}}
                            </p>
                
                            <!-- TRIP INFO -->
                            <h3 style="color:#0f172a; border-bottom:1px solid #e5e7eb; padding-bottom:6px;">
                              Información del Viaje
                            </h3>
                            <p>
                              <b>Destino:</b> {{destiny}}<br>
                              <b>Fecha de salida:</b> {{outDate}}<br>
                              <b>Fecha de regreso:</b> {{returnDate}}<br>
                              <b>Fechas flexibles:</b> {{areDatesFlexible}}
                            </p>
                
                            <!-- BUDGET -->
                            <h3 style="color:#0f172a; border-bottom:1px solid #e5e7eb; padding-bottom:6px;">
                              Presupuesto y Prioridades
                            </h3>
                            <p>
                              <b>Presupuesto:</b> {{budget}}<br>
                              <b>Tipo de nivel:</b> {{levelType}}<br>
                              <b>Prioridad:</b> {{priority}}
                            </p>
                
                            <!-- TRAVELERS -->
                            <h3 style="color:#0f172a; border-bottom:1px solid #e5e7eb; padding-bottom:6px;">
                              Información de Viajeros
                            </h3>
                            <p>
                              <b>Total de viajeros:</b> {{totalTravelers}}<br>
                              <b>Adultos:</b> {{totalAdults}}<br>
                              <b>Menores:</b> {{totalMinors}}<br>
                              <b>Edades de menores:</b> {{minorsAges}}<br>
                              <b>Bebés viajando:</b> {{areBabiesTraveling}}
                            </p>
                
                            <!-- PREFERENCES -->
                            <h3 style="color:#0f172a; border-bottom:1px solid #e5e7eb; padding-bottom:6px;">
                              Preferencias del Viaje
                            </h3>
                            <p>
                              <b>Tipo de viaje:</b> {{tripType}}<br>
                              <b>Tema del viaje:</b> {{tripTheme}}
                            </p>
                
                            <!-- COMMENTS -->
                            <h3 style="color:#0f172a; border-bottom:1px solid #e5e7eb; padding-bottom:6px;">
                              Comentarios Adicionales
                            </h3>
                            <p style="background-color:#f8fafc; padding:12px; border-radius:6px; border:1px solid #e5e7eb;">
                              {{comments}}
                            </p>
                
                          </td>
                        </tr>
                
                        <!-- Footer -->
                        <tr>
                          <td style="background-color:#f1f5f9; padding:15px; text-align:center; font-size:12px; color:#64748b;">
                            Este correo fue generado automáticamente desde el formulario de cotización.
                          </td>
                        </tr>
                
                      </table>
                      <!-- /Contenedor -->
                
                    </td>
                  </tr>
                </table>
                
                </body>
                </html>
                """;

        mailContent = mailContent.replace("{{completeName}}", request.completeName())
                .replace("{{email}}", request.email())
                .replace("{{phone}}", request.phone())
                .replace("{{countryCity}}", request.countryCity())
                .replace("{{destiny}}", request.destiny())
                .replace("{{outDate}}", request.outDate())
                .replace("{{returnDate}}", request.returnDate())
                .replace("{{areDatesFlexible}}", request.areDatesFlexible() ? "Sí" : "No")
                .replace("{{budget}}", request.budget())
                .replace("{{levelType}}", request.levelType())
                .replace("{{priority}}", request.priority())
                .replace("{{totalTravelers}}", String.valueOf(request.totalTravelers()))
                .replace("{{totalAdults}}", String.valueOf(request.totalAdults()))
                .replace("{{totalMinors}}", String.valueOf(request.totalMinors()))
                .replace("{{minorsAges}}", request.minorsAges())
                .replace("{{areBabiesTraveling}}", request.areBabiesTraveling() ? "Sí" : "No")
                .replace("{{tripType}}", request.tripType())
                .replace("{{tripTheme}}", request.tripTheme())
                .replace("{{comments}}", request.comments().isBlank() ? "(Ninguno)" : request.comments());
        // TODO: Handle exceptions properly
        try {
            mimeMessage.setSubject("Detalles enviados desde el formulario cotizacion web de " + request.completeName());
            mimeMessage.setContent(mailContent, "text/html; charset=utf-8");
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("Error sending email: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
