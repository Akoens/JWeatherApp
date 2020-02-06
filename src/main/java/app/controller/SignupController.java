package app.controller;

import app.util.Path;
import app.util.RequestUtil;
import app.util.ViewUtil;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.Map;

import static app.Main.userStore;
import static app.util.RequestUtil.removeSessionSignupError;

public class SignupController {
    public static Route serveSignupPage = (Request request, Response response) -> {
        Map<String, Object> model = new HashMap<>();
        model.put("signupError", removeSessionSignupError(request));
        return ViewUtil.render(request, model, Path.Template.SIGNUP);
    };

    public static Route handleSignupPost = (Request request, Response response) -> {
        Map<String, Object> model = new HashMap<>();
        model.put("signupError", removeSessionSignupError(request));

        //Password checks
        String email = RequestUtil.getQueryEmail(request);
        int authLevel = RequestUtil.getQueryAuthLevel(request);
        String password = RequestUtil.getQueryPassword(request);
        String confirmPassword = RequestUtil.getQueryConfirmPassword(request);

        //TODO
        if(!isValidEmailAddress(email)){
            model.put("signupError", "Email is not valid");
            return ViewUtil.render(request, model, Path.Template.SIGNUP);
        }else if(!(password.equals(confirmPassword))){
            model.put("signupError", "The passwords are not the same");
            return ViewUtil.render(request, model, Path.Template.SIGNUP);
        }else if(!passwordIsValid(password)){
            model.put("signupError", "The passwords is not valid! \\n It must contain: a digit, a lowercase letter, a uppercase letter, a special character and must be longer than 8 characters");
            return ViewUtil.render(request, model, Path.Template.SIGNUP);
        }

        //Add new user
        userStore.addUser(email, UserController.generateHashedPassword(password), authLevel);
        System.out.println(userStore.getAllEmails());
        return ViewUtil.render(request, model, Path.Template.SIGNUP);
    };

    private static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
    private static boolean passwordIsValid(String password) {
        return ( (password.length() >= 8 )
                || (password.matches("(?=.*[0-9]).*"))
                || (password.matches("(?=.*[a-z]).*"))
                || (password.matches("(?=.*[A-Z]).*"))
                || (password.matches("(?=.*[~!@#$%^&*()_-]).*")));
    }
}

