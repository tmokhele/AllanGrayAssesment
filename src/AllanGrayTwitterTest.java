import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class AllanGrayTwitterTest {
    public static void main(String[] args) {

        HashMap userFollowing = new HashMap<String, ArrayList>();
        HashMap tweeterMessages = new HashMap<String, ArrayList>();
        ArrayList<String> userList = new ArrayList<>();

        try {
            BufferedReader users = null;
            BufferedReader tweets = null;
            try {
                InputStream userResourceStream = AllanGrayTwitterTest.class.getResourceAsStream("/user.txt");
                InputStream tweeterResourceStream = AllanGrayTwitterTest.class.getResourceAsStream("/tweet.txt");
                users = new BufferedReader( new InputStreamReader(userResourceStream));
                tweets = new BufferedReader(new InputStreamReader(tweeterResourceStream));
            } catch (Exception e) {
                e.printStackTrace();
            }
            String user;
            String follower;
            String userKey;
            while ((user = users.readLine()) != null) {
                String[] strArray = user.split(" follows |, ");
                follower = strArray[0];
                if (!userList.contains(follower)) {
                    userList.add(follower);
                }
                for (int i = 1; i < strArray.length; i++) {
                    userKey = strArray[i];
                    if (userFollowing.containsKey(userKey)) {
                        List<String> userExistList = (ArrayList) userFollowing.get(userKey);
                        if (!userExistList.contains(follower)) {
                            userExistList.add(follower);
                        }
                        userFollowing.put(userKey, userExistList);
                    } else {

                        List<String> followersList = new ArrayList<>();
                        followersList.add(follower);
                        userFollowing.put(userKey, followersList);

                    }
                    if (!userList.contains(userKey)) {
                        userList.add(userKey);
                    }

                }
            }
            String message;
            String tweet;
            String tweetUser;
            while ((message = tweets.readLine()) != null) {
                String[] tweetsArray = message.split(">");
                tweetUser = tweetsArray[0];
                tweet = tweetsArray[1];
                List<String> existingTweets;
                if (tweeterMessages.containsKey(tweetUser)) {
                    existingTweets = (ArrayList) tweeterMessages.get(tweetUser);


                } else {
                    existingTweets = new ArrayList<>();

                }
                existingTweets.add("@" + tweetUser + ": " + tweet);
                tweeterMessages.put(tweetUser, existingTweets);

                ArrayList<String> userFollowers = (ArrayList) userFollowing.get(tweetUser);
                if (userFollowers != null) {
                    for (String following : userFollowers) {

                        if (tweeterMessages.containsKey(following)) {

                            List<String> folowerTweets = (ArrayList) tweeterMessages.get(following);
                            folowerTweets.add("@" + tweetUser + ": " + tweet);
                            tweeterMessages.put(following, folowerTweets);


                        } else {

                            List<String> tweetFollower = new ArrayList<>();
                            tweetFollower.add("@" + tweetUser + ": " + tweet);
                            tweeterMessages.put(following, tweetFollower);
                        }

                    }


                }


            }

            Collections.sort(userList);
            for (String key : userList) {
                System.out.println(key);
                if (tweeterMessages.containsKey(key)) {
                    List<String> sortedList = (ArrayList) tweeterMessages.get(key);

                    for (String s : sortedList) {

                        System.out.println(s);
                    }
                } else {
                    System.out.println();
                }
            }
            users.close();
            tweets.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
