# myAppTest
Recent Bitcoin Transactions App

Libraries used: Gson, OkHTTP for api and websocket calls, android-ktx

Imporvements possible but not optimised due to time constraints:
1. Better package structure based on functionality of classes
2. Use of fragment instead of adding all code to activity
3. Adding a layer of repository instead of calling apis directly from the view model, this would improve the testability.
4. Adding certain request bodies to const vals and using dimens file for margin and sizes.
