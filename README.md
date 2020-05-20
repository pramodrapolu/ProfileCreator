# ProfileCreator

Profile Creator is simple app which allows user to create profiles.

Used MVVM architecture. Made sure Views are as dumb as they can by making sure it just listens on things(like Live data's from the view model) it needs. And View model takes care of posting the data through LiveData.
Though the app isn't complex, just to make sure business logic is/can be separated in to View Models.

Made sure the layouts are designed in a way they can be reused and views are talkback enabled for Accessibility purposes.

Used Android Architecture components like ViewModels, LiveData

Also used ContraintLayouts so that views scales on different devices.

Used Glide library to load the pictures into the view easily. It handles the orientation/resizing of the pictures which was the main reason I had to use it.
