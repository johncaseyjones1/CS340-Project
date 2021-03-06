package edu.byu.cs.tweeter.model.net;

import android.util.Pair;

import java.util.Calendar;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import edu.byu.cs.tweeter.model.domain.status.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.domain.status.Tag;
import edu.byu.cs.tweeter.model.domain.status.URL;
import edu.byu.cs.tweeter.util.ExtractTagsAndLinksUtil;

public class FakeDatabase {
    private static FakeDatabase instance;

    public static FakeDatabase getInstance() {
        if (instance == null) {
            instance = new FakeDatabase();
        }
        return instance;
    }
    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
    private static final String FEMALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png";
    private final User user1 = new User("Allen", "Anderson", MALE_IMAGE_URL);
    private final User user2 = new User("Amy", "Ames", FEMALE_IMAGE_URL);
    private final User user3 = new User("Bob", "Bobson", MALE_IMAGE_URL);
    private final User user4 = new User("Bonnie", "Beatty", FEMALE_IMAGE_URL);
    private final User user5 = new User("Chris", "Colston", MALE_IMAGE_URL);
    private final User user6 = new User("Cindy", "Coats", FEMALE_IMAGE_URL);
    private final User user7 = new User("Dan", "Donaldson", MALE_IMAGE_URL);
    private final User user8 = new User("Dee", "Dempsey", FEMALE_IMAGE_URL);
    private final User user9 = new User("Elliott", "Enderson", MALE_IMAGE_URL);
    private final User user10 = new User("Elizabeth", "Engle", FEMALE_IMAGE_URL);
    private final User user11 = new User("Frank", "Frandson", MALE_IMAGE_URL);
    private final User user12 = new User("Fran", "Franklin", FEMALE_IMAGE_URL);
    private final User user13 = new User("Gary", "Gilbert", MALE_IMAGE_URL);
    private final User user14 = new User("Giovanna", "Giles", FEMALE_IMAGE_URL);
    private final User user15 = new User("Henry", "Henderson", MALE_IMAGE_URL);
    private final User user16 = new User("Helen", "Hopwell", FEMALE_IMAGE_URL);
    private final User user17 = new User("Igor", "Isaacson", MALE_IMAGE_URL);
    private final User user18 = new User("Isabel", "Isaacson", FEMALE_IMAGE_URL);
    private final User user19 = new User("Justin", "Jones", MALE_IMAGE_URL);
    private final User user20 = new User("Jill", "Johnson", FEMALE_IMAGE_URL);
    private final User testUser = new User("Test", "User", "@testUser", MALE_IMAGE_URL);

    private final Vector<User> users = new Vector<>();


    private FakeDatabase() {
        this.users.add(user1);
        this.users.add(user2);
        this.users.add(user3);
        this.users.add(user4);
        this.users.add(user5);
        this.users.add(user6);
        this.users.add(user7);
        this.users.add(user8);
        this.users.add(user9);
        this.users.add(user10);
        this.users.add(user11);
        this.users.add(user12);
        this.users.add(user13);
        this.users.add(user14);
        this.users.add(user15);
        this.users.add(user16);
        this.users.add(user17);
        this.users.add(user18);
        this.users.add(user19);
        this.users.add(user20);


        Vector<Integer> userInts = new Vector<>();
        userInts.add(0);
        userInts.add(1);
        userInts.add(2);
        userInts.add(3);
        userInts.add(4);
        userInts.add(5);
        userInts.add(6);
        userInts.add(7);
        userInts.add(8);
        userInts.add(9);
        userInts.add(10);
        userInts.add(11);
        userInts.add(12);
        userInts.add(13);
        userInts.add(14);
        userInts.add(15);
        userInts.add(16);
        userInts.add(17);
        userInts.add(18);
        userInts.add(19);

        testUser.getFollowing().addAll(userInts);
        testUser.getFollowers().addAll(userInts);
        Random rand = new Random(0);
        for (int userNum = 0; userNum < 20; ++userNum) {
            //add 5 random followers

            Vector<Integer> intsSoFar = new Vector<>();
            while (users.elementAt(userNum).getFollowers().size() < 5) {
                int randomInt = rand.nextInt(19);
                //Add the user as a follower if not the same as the user that it's being added to
                if (randomInt != userNum && !intsSoFar.contains(randomInt)) {
                    users.elementAt(userNum).getFollowers().add(randomInt);
                    intsSoFar.add(randomInt);
                }
            }
            intsSoFar.clear();

            //add 5 random followees
            while (users.elementAt(userNum).getFollowing().size() < 5) {
                int randomInt = rand.nextInt(19);
                //Add the user as a follower if not the same as the user that it's being added to
                if (randomInt != userNum && !intsSoFar.contains(randomInt)) {
                    users.elementAt(userNum).getFollowing().add(randomInt);
                    intsSoFar.add(randomInt);
                }
            }
            intsSoFar.clear();

            // Create 1 generic status

            String content = "This is my fake status";
            //Pair<Vector<Tag>, Vector<URL>> tagsAndLinks = ExtractTagsAndLinksUtil.parseContent(content);
            users.elementAt(userNum).getStatuses().add(new Status(
                    users.elementAt(userNum).getAlias(),
                    content,
                    new Vector<>(), new Vector<>(),
                    Calendar.getInstance().getTime()));
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (Exception e) {
                System.out.println("Couldn't wait!!");
            }

        }

        this.users.add(testUser);
    }

    public boolean addUser(User user) {
        this.users.add(user);
        return true;
    }

    public Vector<User> getUsers() {
        return users;
    }
}
