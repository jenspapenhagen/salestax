/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.papenhagen.salestax.control;

/**
 * Error Code with a (german) description
 *
 * @author Jens Papenhagen
 */
public enum ErrorCode {
    ERROR200("Die angefragte USt-IdNr. ist gültig."),
    ERROR201("Die angefragte USt-IdNr. ist ungültig."),
    ERROR202("Die angefragte USt-IdNr. ist ungültig. "
            + "Sie ist nicht in der Unternehmerdatei des betreffenden EU-Mitgliedstaates registriert. "
            + "Hinweis: Ihr Geschäftspartner kann seine gültige USt-IdNr. bei der für ihn zuständigen Finanzbehördein Erfahrung bringen."
            + " Möglicherweise muss er einen Antrag stellen, damit seine USt-IdNr. in die Datenbank aufgenommen wird."),
    ERROR203("Die angefragte USt-IdNr. ist ungültig. Sie ist erst ab dem ... gültig (siehe Feld 'Gueltig_ab')."),
    ERROR204("Die angefragte USt-IdNr. ist ungültig. Sie war im Zeitraum von bis ... gültig (siehe Feld 'Gueltig_ab' und 'Gueltig_bis')."),
    ERROR205("Ihre Anfrage kann derzeit durch den angefragten EU-Mitgliedstaat oder aus anderen Gründen nicht beantwortet werden. "
            + "Bitte versuchen Sie es später noch einmal. "
            + "Bei wiederholten Problemen wenden Sie sich bitte an das Bundeszentralamt für Steuern - Dienstsitz Saarlouis."),
    ERROR206("Ihre deutsche USt-IdNr. ist ungültig. Eine Bestätigungsanfrage ist daher nicht möglich. "
            + "Den Grund hierfür können Sie beim Bundeszentralamt für Steuern - Dienstsitz Saarlouis - erfragen."),
    ERROR207("Ihnen wurde die deutsche USt-IdNr. ausschliesslich zu Zwecken der Besteuerung des innergemeinschaftlichen Erwerbs erteilt."
            + "Sie sind somit nicht berechtigt, Bestätigungsanfragen zu stellen. "),
    ERROR208("Für die von Ihnen angefragte USt-IdNr. läuft gerade eine Anfrage von einem anderen Nutzer. "
            + "Eine Bearbeitung ist daher nicht möglich. Bitte versuchen Sie es später noch einmal."),
    ERROR209("Die angefragte USt-IdNr. ist ungültig. Sie entspricht nicht dem Aufbau der für diesen EU-Mitgliedstaat gilt."),
    ERROR210("Die angefragte USt-IdNr. ist ungültig. Sie entspricht nicht den Prüfziffernregeln die für diesen EU-Mitgliedstaat gelten."),
    ERROR211("Die angefragte USt-IdNr. ist ungültig. Sie enthält unzulässige Zeichen (wie z.B. Leerzeichen oder Punkt oder Bindestrich usw.)."),
    ERROR212("Die angefragte USt-IdNr. ist ungültig. Sie enthält ein unzulässiges Länderkennzeichen."),
    ERROR213("Die Abfrage einer deutschen USt-IdNr. ist nicht möglich."),
    ERROR214("Ihre deutsche USt-IdNr. ist fehlerhaft. Sie beginnt mit 'DE' gefolgt von 9 Ziffern."),
    ERROR215("Ihre Anfrage enthält nicht alle notwendigen Angaben für eine einfache Bestätigungsanfrage (Ihre deutsche USt-IdNr. und die ausl. USt-IdNr.)."
            + "Ihre Anfrage kann deshalb nicht bearbeitet werden."),
    ERROR216("Ihre Anfrage enthält nicht alle notwendigen Angaben für eine qualifizierte Bestätigungsanfrage (Ihre deutsche USt-IdNr., die ausl."
            + "USt-IdNr., Firmenname einschl. Rechtsform und Ort). Es wurde eine einfache Bestätigungsanfrage durchgeführt mit folgenden Ergebnis: "
            + "Die angefragte USt-IdNr. ist gültig."),
    ERROR217("Bei der Verarbeitung der Daten aus dem angefragten EU-Mitgliedstaat ist ein Fehler aufgetreten. Ihre Anfrage kann deshalb nicht bearbeitet werden."),
    ERROR218("Eine qualifizierte Bestätigung ist zur Zeit nicht möglich. Es wurde eine einfache Bestätigungsanfrage mit folgendem Ergebnis durchgeführt:"
            + "Die angefragte USt-IdNr. ist gültig."),
    ERROR219("Bei der Durchführung der qualifizierten Bestätigungsanfrage ist ein Fehler aufgetreten. "
            + "Es wurde eine einfache Bestätigungsanfrage mit folgendem Ergebnis durchgeführt: Die angefragte USt-IdNr. ist gültig."),
    ERROR220("Bei der Anforderung der amtlichen Bestätigungsmitteilung ist ein Fehler aufgetreten. Sie werden kein Schreiben erhalten."),
    ERROR221("Die Anfragedaten enthalten nicht alle notwendigen Parameter oder einen ungültigen Datentyp. "
            + "Weitere Informationen erhalten Sie bei den Hinweisen zum Schnittstelle"),
    ERROR222("Die angefragte USt-IdNr. ist gültig. Bitte beachten Sie die Umstellung auf ausschließlich HTTPS (TLS 1.2) zum 07.01.2019."),
    ERROR999("Eine Bearbeitung Ihrer Anfrage ist zurzeit nicht möglich. Bitte versuchen Sie es später noch einmal.");

    private final String description;

    private ErrorCode(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
