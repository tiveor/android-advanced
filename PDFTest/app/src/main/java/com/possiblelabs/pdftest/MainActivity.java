package com.possiblelabs.pdftest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfWriter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "PDFTest";

    private static final String FOLDER = "/PDFTest";
    private static final String FILE_NAME = "test.pdf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void generatePDF(View view) {
        Document doc = new Document();


        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + FOLDER;

            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();

            File file = new File(dir, FILE_NAME);
            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter.getInstance(doc, fOut);

            doc.open();

            Paragraph p1 = new Paragraph("This is the first parragraph");
            Font paraFont = new Font(Font.COURIER);
            p1.setAlignment(Paragraph.ALIGN_LEFT);
            p1.setFont(paraFont);
            doc.add(p1);

            Paragraph p2 = new Paragraph("Other parragraph");
            Font paraFont2 = new Font(Font.HELVETICA, 18.0f, Color.BLUE);
            p2.setAlignment(Paragraph.ALIGN_CENTER);
            p2.setFont(paraFont2);
            doc.add(p2);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.logo);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            Image myImg = Image.getInstance(stream.toByteArray());
            myImg.setAlignment(Image.MIDDLE);
            doc.add(myImg);

            Phrase footerText = new Phrase("FOOTER AT THE END???");
            HeaderFooter pdfFooter = new HeaderFooter(footerText, true);
            doc.setFooter(pdfFooter);

            Phrase headerText = new Phrase("HEADER TEXT");
            HeaderFooter pdfHeader = new HeaderFooter(headerText, true);
            doc.setHeader(pdfHeader);

            Toast.makeText(this, "It works!!!", Toast.LENGTH_SHORT).show();

        } catch (DocumentException de) {
            Log.e(TAG, "DocumentException:" + de.getMessage());
            de.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG, "ioException:" + e.getMessage());
            e.printStackTrace();
        } finally {
            doc.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
