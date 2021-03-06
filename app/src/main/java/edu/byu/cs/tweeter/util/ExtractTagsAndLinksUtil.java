package edu.byu.cs.tweeter.util;

import android.util.Pair;
import android.util.Patterns;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.domain.status.Tag;
import edu.byu.cs.tweeter.model.domain.status.URL;
import edu.byu.cs.tweeter.model.net.FakeDatabase;

public class ExtractTagsAndLinksUtil {

    public static Pair<Vector<Tag>, Vector<URL>> parseContent(String content) {
        Vector<Tag> tags = new Vector<>();
        Vector<URL> links = new Vector<>();
        tags = extractTags(content);
        links = extractLinks(content);

        Pair<Vector<Tag>, Vector<URL>> output = new Pair<>(tags, links);
        return output;
    }

    private static Vector<Tag> extractTags(String content) {

        Vector<Tag> tags = new Vector<>();
        for (int i = 0; i < content.length(); i++) {
            if (content.charAt(i) == '@') {
                int tagLength = 0;
                int tagStart = i;
                while (content.charAt(i) != ' ') {
                    tagLength++;
                    i++;
                    if (i >= content.length()) {
                        i = content.length() - 1;
                        break;
                    }
                }
                FakeDatabase db = getDatabase();
                User theUser = null;
                for (User user : db.getUsers()) {
                    System.out.println(user.getAlias() + " compared with " + content.substring(tagStart, tagStart + tagLength));
                    if (user.getAlias().equals(content.substring(tagStart, tagStart + tagLength))) {
                        theUser = user;
                    }
                }
                tags.add(new Tag(theUser, tagStart, tagLength));
            }
        }
        return tags;
    }

    private static Vector<URL> extractLinks(String content) {
        Pattern HTTP_REGEX = Pattern.compile("\\bhttps?://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");
        Matcher contentMatcher = HTTP_REGEX.matcher(content);
        Vector<URL> links = new Vector<>();
        while (contentMatcher.find()) {
            URL newURL = new URL(contentMatcher.group(), contentMatcher.start(), (contentMatcher.end() - contentMatcher.start()));
            links.add(newURL);
        }
        return links;
    }

    static FakeDatabase getDatabase() {
        return FakeDatabase.getInstance();
    }
}
