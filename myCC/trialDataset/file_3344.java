    public boolean isValidPage(BookPage page) {
        boolean isValid = false;
        try {
            if (page.getType() == BookPage.TYPE_underscoreRESOURCE) {
                BookPagePreviewPanel panel = new BookPagePreviewPanel(dControl, true);
                panel.setCurrentBookPage(page);
                isValid = !page.getUri().equals("") && panel.isValid();
            } else if (page.getType() == BookPage.TYPE_underscoreURL) {
                URL url = new URL(page.getUri());
                url.openStream().close();
                isValid = true;
            } else if (page.getType() == BookPage.TYPE_underscoreIMAGE) {
                if (page.getUri().length() > 0) isValid = true;
            }
        } catch (Exception e) {
            isValid = false;
        }
        return isValid;
    }

