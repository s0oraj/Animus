![banner](https://user-images.githubusercontent.com/42529024/198926355-20252b0a-fcce-403d-8d28-2b5ad71b2550.png)


 # Animus - The social media app

 A social media android application which stores users data in  [Firebase Database](https://firebase.google.com/docs/firestore).

 It uses native android java and xml as frontend and logic of the app.



 * Users can add posts, like other users posts and comment on posts, they can also find and follow others using the search button
 * Users can also add new status which their followers can see. This status can be a video or image file which lasts for 24 hours.
 * Messaging feature allows users to stay in touch with their friends!


 ## Screenshots

![Screeshot_1](https://user-images.githubusercontent.com/42529024/198926752-693c765d-e8a0-4322-8a05-bb8b274cc602.png)
![Screenshot_2](https://user-images.githubusercontent.com/42529024/198926742-61272946-a468-42f8-bb3b-ae085d1bb005.png)



 ## Libraries used


 * [Glide](https://github.com/bumptech/glide)
 * [Picasso](https://github.com/square/picasso)
 * [ExoPlayer](https://github.com/google/ExoPlayer)
 * [Video-Trimmer](https://github.com/a914-gowtham/android-video-trimmer)
 * [Edmodo-image-cropper] https://github.com/ArthurHub/Android-Image-Cropper
 * [Butter-Knife] https://github.com/JakeWharton/butterknife
 * [hdodenhof:circleimageview] https://github.com/hdodenhof/CircleImageView
 * [karumi:dexter] https://github.com/Karumi/Dexter
 * [Android-SpinKit] https://github.com/ybq/Android-SpinKit
  * [Slidableactivity] https://github.com/r0adkll/Slidr




https://github.com/bumptech/glide
 ## Features

 -  Users can set the number of recent post they want to see. For example, if a user sets his preference to three posts then the user is only shown three recent posts from the ScienceGlass blog
 -  Swapping and Deleting of Data: Users can tap on a post and swipe right or left to remove it from screen. Users can also tap on a post hold on it and change its location by moving it up or down the list of available posts. Refresh button will restore all data back to its original configuration.
 -  Change layout: Users can customize what layout they want to see their posts in. Two layouts are provided: LinearLayout and GridLayout.
 
 
## API Reference

#### Get a list of matching posts.

```http
  GET /sites/$site/posts/
```

| Method | URL    | Requires authentication?     |
| :-------- | :------- | :------------------------- |
| `GET` | `https://public-api.wordpress.com/rest/v1.1/sites/$site/posts/` | No |

#### Method Parameters


| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `$site`      | `(int string)` | Site ID or domain |
 
 Our Website (to be replaced by $site) : https://cosmos.home.blog


