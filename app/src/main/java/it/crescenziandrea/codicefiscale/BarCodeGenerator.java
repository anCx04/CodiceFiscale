package it.crescenziandrea.codicefiscale;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;

import java.util.Map;

public class BarCodeGenerator<codeWriter> {
    Bitmap bitmap = null;

    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    Bitmap encodeAsBitmap(String code, BarcodeFormat format, int img_width, int img_height) throws WriterException {
        String codeToEncode = code;
        if(codeToEncode == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = null;
        String encoding = guess
    }
}
