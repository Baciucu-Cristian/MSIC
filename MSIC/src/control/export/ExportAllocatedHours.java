package control.export;

import Models.HoursFormations;
import Models.Professor;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class ExportAllocatedHours extends PdfPageEventHelper{
    ArrayList<Font> tableBodyFonts;
    public String rector;
    public String director_departament;
    public String decan;
    PdfPTable table1;
    PdfPTable table2;
    PdfPTable table3;
    PdfPTable table4;
    String[] romanDigits;
    
    public ExportAllocatedHours()
    {  	
        tableBodyFonts = new ArrayList<Font>();
        tableBodyFonts.add(FontFactory.getFont("Arial", 9, Font.BOLD));
        tableBodyFonts.add(FontFactory.getFont("Arial", 8, Font.BOLD));
        tableBodyFonts.add(FontFactory.getFont("Arial", 8));
        tableBodyFonts.add(FontFactory.getFont("Arial", 9, Font.BOLD));
        tableBodyFonts.add(FontFactory.getFont("Arial", 9, Font.BOLD));
        tableBodyFonts.add(FontFactory.getFont("Arial", 9, Font.BOLD));
        tableBodyFonts.add(FontFactory.getFont("Arial", 9, Font.BOLD));
        tableBodyFonts.add(FontFactory.getFont("Arial", 9, Font.BOLD));
        tableBodyFonts.add(FontFactory.getFont("Arial", 9, Font.BOLD));
        tableBodyFonts.add(FontFactory.getFont("Arial", 12, Font.BOLD));
        tableBodyFonts.add(FontFactory.getFont("Arial", 14, Font.BOLD));
        tableBodyFonts.add(FontFactory.getFont("Arial", 9, Font.BOLD, BaseColor.WHITE));
        tableBodyFonts.get(4).setColor(new BaseColor(0xff, 0x00, 0x00));
        tableBodyFonts.get(5).setColor(new BaseColor(0x00, 0x00, 0xff));
        tableBodyFonts.get(6).setColor(new BaseColor(0x8a, 0x2b, 0xe2));
        tableBodyFonts.get(7).setColor(new BaseColor(0x98, 0x5f, 0x0d));
        tableBodyFonts.get(8).setColor(new BaseColor(0x00, 0x80, 0x00));
        romanDigits = new String[]{"I", "II"};
    }
    
    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase("UNIVERSITATEA DIN CRAIOVA", tableBodyFonts.get(0)), 30, 810, 0);
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase("RECTOR,", tableBodyFonts.get(8)), 30, 55, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("DIRECTOR DEPARTAMENT,", tableBodyFonts.get(0)), document.getPageSize().getWidth() / 2, 55, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT, new Phrase("DECAN,", tableBodyFonts.get(0)), document.getPageSize().getWidth() - 30, 55, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase(rector, tableBodyFonts.get(0)), 30, 40, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(director_departament, tableBodyFonts.get(0)), document.getPageSize().getWidth() / 2, 40, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT, new Phrase(decan, tableBodyFonts.get(0)), document.getPageSize().getWidth() - 30, 40, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(Integer.toString(document.getPageNumber())), document.getPageSize().getWidth() / 2, 15, 0);
    }
    
    public void createParagraph(String text, Document doc, Font font, int align) throws DocumentException, IOException
    {
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(align);
        doc.add(p);
    }
    
    public PdfPCell CreateHeaderCell(String text, BaseColor backgroundColor)
    {
        PdfPCell cell = new PdfPCell(new Phrase(text, tableBodyFonts.get(11)));
        cell.setBackgroundColor(backgroundColor);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }
    
    public PdfPCell CreateBodyCell(String text, int align, Font font)
    {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(align);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }
    
    public void createTableOneAndTwoHeader(PdfPTable table) throws DocumentException
    {
        BaseColor color = new BaseColor(0x16, 0x7f, 0x92);
        table.setWidthPercentage(100.0f);
        table.addCell(CreateHeaderCell("Nume", color));
        table.addCell(CreateHeaderCell("CNP", color));
        table.addCell(CreateHeaderCell("Departament", color));
        table.addCell(CreateHeaderCell("Ore Repartizate", color));
        table.setHeaderRows(1);
        table.setSplitLate(false);
        float[] columnWidths = new float[] {16f, 13f, 13f, 60f};
        table.setWidths(columnWidths);
    }
    
    public void createTableThreeHeader(PdfPTable table) throws DocumentException
    {
        BaseColor color = new BaseColor(0x16, 0x7f, 0x92);
        table.setWidthPercentage(100.0f);
        table.addCell(CreateHeaderCell("Nume", color));
        table.addCell(CreateHeaderCell("CNP", color));
        table.addCell(CreateHeaderCell("Departament", color));
        table.setHeaderRows(1);
        table.setSplitLate(false);
        float[] columnWidths = new float[] {40f, 13f, 47f};
        table.setWidths(columnWidths);
    }
    
    public void createTableFourHeader(PdfPTable table) throws DocumentException
    {
        BaseColor color = new BaseColor(0x16, 0x7f, 0x92);
        table.setWidthPercentage(100.0f);
        table.addCell(CreateHeaderCell("Id", color));
        table.addCell(CreateHeaderCell("Departament", color));
        table.addCell(CreateHeaderCell("Formatiune", color));
        table.addCell(CreateHeaderCell("Materie", color));
        table.addCell(CreateHeaderCell("Activitate", color));
        table.addCell(CreateHeaderCell("Materii Comune", color));
        table.addCell(CreateHeaderCell("Ore", color));
        table.setHeaderRows(1);
        table.setSplitLate(false);
        float[] columnWidths = new float[] {3f, 15f, 17f, 16f, 10f, 35f, 4f};
        table.setWidths(columnWidths);
    }
    
    public void addEmptyRow(PdfPTable table)
    {
        for (int i = 0; i < table.getNumberOfColumns(); i++)
        {
            table.addCell(CreateBodyCell(" ", Element.ALIGN_LEFT, tableBodyFonts.get(2)));
        }
    }
    
    public String exportToPDF(ArrayList<Professor> professors, ArrayList<HoursFormations> unallocatedHours,
            String webSiteLocation, String localPath, int semester) throws DocumentException, FileNotFoundException
    {
        Document document = new Document(PageSize.A4, 40, 40, 60, 70); //left right top bottom
        
        String file = webSiteLocation + localPath;
        
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file)); 
        Font newLine = FontFactory.getFont("Arial", 2, Font.BOLD);
        writer.setPageEvent(this);
        
        document.open();
        table1 = new PdfPTable(4);
        table2 = new PdfPTable(4);
        table3 = new PdfPTable(3);
        table4 = new PdfPTable(7);
        LineSeparator ls = new LineSeparator();
        createTableOneAndTwoHeader(table1);
        createTableOneAndTwoHeader(table2);
        createTableThreeHeader(table3);
        createTableFourHeader(table4);
        for (Professor professor : professors)
        {
            Phrase phrase = new Phrase();
            phrase.add(new Chunk("     POST - BAZA\n\n", tableBodyFonts.get(3)));

            if (!professor.allocatedHours.post_normal.equals(""))
            {
                phrase.add(new Chunk(professor.allocatedHours.post_normal.replaceAll("<br />", "\n") + "\n", tableBodyFonts.get(2)));
            }
            if (!professor.allocatedHours.post_plata_cu_ora.equals(""))
            {
                phrase.add(new Chunk(ls));
                phrase.add(new Chunk("\n\n     POST - PLATA CU ORA\n\n", tableBodyFonts.get(3)));
                phrase.add(new Chunk(professor.allocatedHours.post_plata_cu_ora.replaceAll("<br />", "\n") + "\n", tableBodyFonts.get(2)));
            }
            if (professor.curs + professor.seminar + professor.lucrari_practice + professor.proiect == 0)
            {
                table1.addCell(CreateBodyCell(professor.nume, Element.ALIGN_CENTER, tableBodyFonts.get(1)));
                table1.addCell(CreateBodyCell(professor.CNP, Element.ALIGN_LEFT, tableBodyFonts.get(2)));
                table1.addCell(CreateBodyCell(professor.departament.denumire, Element.ALIGN_LEFT, tableBodyFonts.get(2)));
                table1.addCell(phrase);
            }
            else if (professor.curs + professor.seminar + professor.lucrari_practice + professor.proiect != professor.totalhours)
            {
                phrase.add(new Chunk(ls));
                phrase.add(new Chunk("\n\n     ORE NEREPARTIZATE:\n\n", tableBodyFonts.get(3)));

                if (professor.curs > 0)
                {
                    phrase.add(new Chunk("CURS - " + professor.curs + "\n", tableBodyFonts.get(4)));
                }
                if (professor.seminar > 0)
                {
                    phrase.add(new Chunk("SEMINAR - " + professor.seminar + "\n", tableBodyFonts.get(5)));
                }
                if (professor.lucrari_practice > 0)
                {
                    phrase.add(new Chunk("LABORATOR - " + professor.lucrari_practice + "\n", tableBodyFonts.get(6)));
                }
                if (professor.proiect > 0)
                {
                    phrase.add(new Chunk("PROIECT - " + professor.proiect + "\n", tableBodyFonts.get(7)));
                }

                phrase.add(new Chunk("TOTAL - " + (professor.curs + professor.seminar + professor.lucrari_practice + professor.proiect), tableBodyFonts.get(8)));
                table2.addCell(CreateBodyCell(professor.nume, Element.ALIGN_CENTER, tableBodyFonts.get(1)));
                table2.addCell(CreateBodyCell(professor.CNP, Element.ALIGN_LEFT, tableBodyFonts.get(2)));
                table2.addCell(CreateBodyCell(professor.departament.denumire, Element.ALIGN_LEFT, tableBodyFonts.get(2)));
                table2.addCell(phrase);
            }
            else
            {
                table3.addCell(CreateBodyCell(professor.nume, Element.ALIGN_CENTER, tableBodyFonts.get(1)));
                table3.addCell(CreateBodyCell(professor.CNP, Element.ALIGN_LEFT, tableBodyFonts.get(2)));
                table3.addCell(CreateBodyCell(professor.departament.denumire, Element.ALIGN_LEFT, tableBodyFonts.get(2)));
            }
        }
        
        String comSubjects;
        for (HoursFormations item : unallocatedHours)
        {
            table4.addCell(CreateBodyCell(Integer.toString(item.id), Element.ALIGN_CENTER, tableBodyFonts.get(1)));
            table4.addCell(CreateBodyCell(item.departament, Element.ALIGN_LEFT, tableBodyFonts.get(2)));
            table4.addCell(CreateBodyCell(item.formatiune, Element.ALIGN_LEFT, tableBodyFonts.get(2)));
            table4.addCell(CreateBodyCell(item.materie, Element.ALIGN_LEFT, tableBodyFonts.get(2)));
            table4.addCell(CreateBodyCell(item.activitate, Element.ALIGN_CENTER, tableBodyFonts.get(2)));
            
            comSubjects = "";
            for (HoursFormations common : item.materii_comune)
            {
                comSubjects += common.materie + " -> " + common.activitate + " -> " + common.formatiune + " -> " + common.departament + " -> " + common.id + ", ";
            }
            
            table4.addCell(CreateBodyCell(comSubjects, Element.ALIGN_LEFT, tableBodyFonts.get(2)));
            table4.addCell(CreateBodyCell(Integer.toString(item.ore), Element.ALIGN_CENTER, tableBodyFonts.get(2)));
        }
        
        try
        {
            createParagraph("REPARTIZARE ORE - SEMESTRUL " + romanDigits[semester - 1], document, tableBodyFonts.get(10), Element.ALIGN_CENTER);
            document.add(new Phrase("\n", newLine));
            document.add(new Phrase("\n", newLine));
            createParagraph("        CADRE DIDACTICE CU CATEDRA COMPLETA", document, tableBodyFonts.get(9), Element.ALIGN_LEFT);
            document.add(new Phrase("\n", newLine));
            addEmptyRow(table1);
            document.add(table1);
            document.newPage();
            createParagraph("        CADRE DIDACTICE CU CATEDRA INCOMPLETA", document, tableBodyFonts.get(9), Element.ALIGN_LEFT);
            document.add(new Phrase("\n", newLine));
            addEmptyRow(table2);
            document.add(table2);
            document.newPage();
            createParagraph("        CADRE DIDACTICE FARA CATEDRA", document, tableBodyFonts.get(9), Element.ALIGN_LEFT);
            document.add(new Phrase("\n", newLine));
            addEmptyRow(table3);
            document.add(table3);
            document.newPage();
            createParagraph("        ORE NEREPARTIZATE", document, tableBodyFonts.get(9), Element.ALIGN_LEFT);
            document.add(new Phrase("\n", newLine));
            addEmptyRow(table4);
            document.add(table4);
        }
        catch (IOException ex)
        {
            return "Repartizarea orelor nu a fost exportata in urma unei erori aparute";
        }
        document.close();
        
        File source = new File(file);
        
        Path path = Paths.get(webSiteLocation);
        path = path.getParent().getParent();
        
        String tempPath = path.toAbsolutePath().toString() + "\\web" + localPath;
        File target = new File(tempPath);
        
        try
        {
            Files.copy(source.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException ex)
        {
        }
        
        return null;
    }
}
