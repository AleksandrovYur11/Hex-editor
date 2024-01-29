package service.impl;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import javax.swing.text.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
public class CustomCellEditor extends DefaultCellEditor {

    public CustomCellEditor() {
        super(new JTextField());
        JTextField textField = (JTextField) getComponent();
        textField.setHorizontalAlignment(JTextField.CENTER);

        PlainDocument doc = (PlainDocument) textField.getDocument();
        doc.setDocumentFilter(new HexDocumentFilter());
    }

    private static class HexDocumentFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {
            Document doc = fb.getDocument();
            StringBuilder sb = new StringBuilder();
            sb.append(doc.getText(0, doc.getLength()));
            sb.insert(offset, string);

            if (isHex(sb.toString()) && sb.length() <= 2) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            Document doc = fb.getDocument();
            StringBuilder sb = new StringBuilder();
            sb.append(doc.getText(0, doc.getLength()));
            sb.replace(offset, offset + length, text);

            if (isHex(sb.toString()) && sb.length() <= 2) {
                super.replace(fb, offset, length, text, attrs);
            }
        }

        private boolean isHex(String str) {
            Pattern pattern = Pattern.compile("^[0-9A-Fa-f]+$");
            Matcher matcher = pattern.matcher(str);
            return matcher.matches();
        }
    }
}