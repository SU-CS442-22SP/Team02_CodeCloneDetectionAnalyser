    public void delete(URI uri, Map<?, ?> options) throws IOException {
        try {
            URL url = new URL(uri.toString());
            URLConnection urlConnection = url.openConnection();
            urlConnection.setDoOutput(true);
            if (urlConnection instanceof HttpURLConnection) {
                final HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
                httpURLConnection.setRequestMethod("DELETE");
                int responseCode = httpURLConnection.getResponseCode();
                switch(responseCode) {
                    case HttpURLConnection.HTTP_underscoreOK:
                    case HttpURLConnection.HTTP_underscoreACCEPTED:
                    case HttpURLConnection.HTTP_underscoreNO_underscoreCONTENT:
                        {
                            break;
                        }
                    default:
                        {
                            throw new IOException("DELETE failed with HTTP response code " + responseCode);
                        }
                }
            } else {
                throw new IOException("Delete is not supported for " + uri);
            }
        } catch (RuntimeException exception) {
            throw new Resource.IOWrappedException(exception);
        }
    }

