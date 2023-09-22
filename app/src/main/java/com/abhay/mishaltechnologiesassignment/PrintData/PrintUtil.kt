import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.PrintManager
import android.webkit.WebView
import android.webkit.WebViewClient
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

object PrintUtil {
    fun createAndPrintPdf(context: Context, text: String) {
        val pdfFile = createPdfFromText(text)
        if (pdfFile != null) {
            printPdf(context, pdfFile,text)
        }
    }

    private fun createPdfFromText(text: String): File? {
        return try {
            val pdfFile = File(
                Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS
                ), "text_to_pdf.pdf"
            )
            val document = Document()
            val outputStream: OutputStream = FileOutputStream(pdfFile)
            PdfWriter.getInstance(document, outputStream)
            document.open()
            document.add(Paragraph(text))
            document.close()
            outputStream.close()
            pdfFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun printPdf(context: Context, pdfFile: File,data:String) {
        val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager
        val printAdapter: PrintDocumentAdapter
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            printAdapter = object : PrintDocumentAdapter() {
                override fun onWrite(
                    pages: Array<PageRange?>?,
                    destination: ParcelFileDescriptor,
                    cancellationSignal: CancellationSignal?,
                    callback: WriteResultCallback
                ) {
                    try {
                        val input: InputStream = FileInputStream(pdfFile)
                        val output: OutputStream = FileOutputStream(destination.fileDescriptor)
                        val buffer = ByteArray(1024)
                        var bytesRead: Int
                        while (input.read(buffer).also { bytesRead = it } != -1) {
                            output.write(buffer, 0, bytesRead)
                        }
                        callback.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
                        input.close()
                        output.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }

                override fun onLayout(
                    oldAttributes: PrintAttributes?,
                    newAttributes: PrintAttributes?,
                    cancellationSignal: CancellationSignal,
                    callback: LayoutResultCallback,
                    extras: Bundle?
                ) {
                    if (cancellationSignal.isCanceled()) {
                        callback.onLayoutCancelled()
                        return
                    }
                    val pdi = PrintDocumentInfo.Builder("text_to_pdf.pdf")
                        .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                        .build()
                    callback.onLayoutFinished(pdi, true)
                }
            }
        } else {
            val webView = WebView(context)

            webView.settings.javaScriptEnabled = true

            val htmlText = "<html><body><h1>Hello, WebView!</h1><p>$data</p></body></html>"

            webView.loadData(htmlText, "text/html", "UTF-8")

            webView.webViewClient = WebViewClient()

            printAdapter = webView.createPrintDocumentAdapter()

        }
        val builder = PrintAttributes.Builder()
        builder.setMinMargins(PrintAttributes.Margins.NO_MARGINS)
        builder.setMediaSize(PrintAttributes.MediaSize.ISO_A4)
        printManager.print("text_to_pdf.pdf", printAdapter, builder.build())
    }
}