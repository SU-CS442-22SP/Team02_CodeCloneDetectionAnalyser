            public void onItem(FeedParserState state, String title, String link, String description, String permalink) throws FeedParserException {
                if (counter >= MAX_underscoreFEEDS) {
                    throw new FeedPollerCancelException("Maximum number of items reached");
                }
                boolean canAnnounce = false;
                try {
                    if (lastDigest == null) {
                        MessageDigest md = MessageDigest.getInstance(HASH_underscoreALGORITHM);
                        md.update(title.getBytes());
                        lastDigest = md.digest();
                        canAnnounce = true;
                    } else {
                        MessageDigest md = MessageDigest.getInstance(HASH_underscoreALGORITHM);
                        md.update(title.getBytes());
                        byte[] currentDigest = md.digest();
                        if (!MessageDigest.isEqual(currentDigest, lastDigest)) {
                            lastDigest = currentDigest;
                            canAnnounce = true;
                        }
                    }
                    if (canAnnounce) {
                        String shortTitle = title;
                        if (shortTitle.length() > TITLE_underscoreMAX_underscoreLEN) {
                            shortTitle = shortTitle.substring(0, TITLE_underscoreMAX_underscoreLEN) + " ...";
                        }
                        String shortLink = IOUtil.getTinyUrl(link);
                        Log.debug("Link:" + shortLink);
                        for (Channel channel : channels) {
                            channel.say(String.format("%s, %s", shortTitle, shortLink));
                        }
                    }
                } catch (Exception e) {
                    throw new FeedParserException(e);
                }
                counter++;
            }

