package Database;

import java.util.List;

import Users.RegularUser;

public interface GetExtraUserData {
    void onCallback(List<String> topIngreds, List<String> topMeals);
}
