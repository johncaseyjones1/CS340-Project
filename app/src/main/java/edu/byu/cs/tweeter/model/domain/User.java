package edu.byu.cs.tweeter.model.domain;

import java.io.Serializable;
import java.util.Objects;
import java.util.Vector;

import edu.byu.cs.tweeter.model.domain.status.Status;

/**
 * Represents a user in the system.
 */
public class User implements Comparable<User>, Serializable {

    private final String firstName;
    private final String lastName;
    private String alias;
    private final String imageUrl;
    private byte [] imageBytes;
    private Vector<Integer> followers = new Vector<>();
    private Vector<Integer> following = new Vector<>();
    private final Vector<Status> statuses = new Vector<>();

    public User(String firstName, String lastName, String imageURL) {
        this(firstName, lastName, String.format("@%s%s", firstName, lastName), imageURL);
    }

    public User(String firstName, String lastName, String alias, String imageURL) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.alias = alias;
        this.imageUrl = imageURL;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getName() {
        return String.format("%s %s", firstName, lastName);
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public byte [] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return alias.equals(user.alias);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alias);
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", alias='" + alias + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    public Vector<Integer> getFollowers() {
        return followers;
    }

    public void setFollowers(Vector<Integer> followers) {
        this.followers = followers;
    }

    public Vector<Integer> getFollowing() {
        return following;
    }

    public void setFollowing(Vector<Integer> following) {
        this.following = following;
    }

    public Vector<Status> getStatuses() {
        return statuses;
    }

    @Override
    public int compareTo(User user) {
        return this.getAlias().compareTo(user.getAlias());
    }
}
