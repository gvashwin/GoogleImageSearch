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
  * [x] User can configure advanced search filters such as
   * Size (small, medium, large, extra-large)
   * Color filter (black, blue, brown, gray, green, etc...)
   * Type (faces, photo, clip art, line art)
   * Site (espn.com)
  * [x] Subsequent searches will have any filters applied to the search results
  * [x] User can tap on any image in results to see the image full-screen
  * [x] User can scroll down “infinitely” to continue loading more image results (up to 8 pages)
  * [x] Network failures are handled
  * [x] Serch widget in action bar. Users will enter a query using action bar search widget.
  * [x] User can share an image to their friends or email it to themselves.
  * [x] Search filters are prompted and recorded using dialog fragments.
  * [x] Improved user experience with welcome messages and messages on network error.
  * [x] Used the StaggeredGridView to display and improve the grid of image results.
  * [x] User can zoom or pan images displayed in full-screen detail view.
  * [x] User can clear all the search filters using a single clear button.
