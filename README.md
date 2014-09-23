#Google Image Searh Client

This is a simple android app to do gogole image search
The app was developed using the [Google Image API](https://developers.google.com/image-search/v1/jsondevguide#json_reference). 
Note this API is deprecated.

##Third-Party Librarys Used
 * Implemented staggered grid view using this [library](https://github.com/f-barth/AndroidStaggeredGrid)
 * Implemented image pan/zoom using this [library](https://github.com/MikeOrtiz/TouchImageView)

##Completed User Stories
  * [x] User can enter a search query that will display a grid of image results from the Google Image API
  * [x] User can click on "settings" which allows selection of advanced search options to filter results
  * [x]User can configure advanced search filters such as
   * Size (small, medium, large, extra-large)
   * Color filter (black, blue, brown, gray, green, etc...)
   * Type (faces, photo, clip art, line art)
   * Site (espn.com)
  * [x] App user can see the like count on each popular photo.
  * [x] User's profile pic is also displayed.
  * [x] App user can pull to refresh the photo stream at any point and it will display the popular photos at that time.
  * [x] App user will also be able to see the two most recent comments on the photo.
  * [x] The Display of each photo is streamlined with that of the actual instagram app.
  * [x] Circular image view is used to diaply the profile pic of the user.
  * [x] Improved user experience with smooth scrolling using view holder pattern
