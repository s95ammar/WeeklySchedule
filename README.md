# WeeklySchedule
An android app for organizing weekly schedules in tables using events and colorful categories.

- [In progress](#In-progress)
- [App preview](#App-Preview)
  - [Navigation drawer & its fragments](#Navigationdrawer&itsfragments)
  - [Schedules](#Schedules)
    - [Adding](#Adding)
    - [Renaming](#Renaming)
    - [Deleting](#Deleting)
    - [Changing active schedule](#Changing-active-schedule)
  - [Categories](#Categories)
    - [Adding](#Adding-1)
    - [Editing](#Editing-1)
    - [Deleting](#Deleting-1)
  - [Events](#Events)
    - [Adding](#Adding-2)
    - [Editing](#Editing-2)
    - [Deleting](#Deleting-2)

## In progress

> * Code:
>   - [x] Convert to `Kotlin`. (The awfully written Java version is still on another branch).
>   - [x] Rewrite with `MVVM Architecture` using `Room` for **SQLite** & `LiveData`.
>   - [x] Apply **Dependency Injection** using `Dagger2`
> 
> * App interface & functionality:
>   - [x] Add a **multi-choice dialog** for creating multiple events in multiple days at once.
>   - [x] Enable **2-week schedules**.
>   - [ ] Enable **multi-day long events**.

## App preview

> ### Navigation drawer & its fragments
>> ![1_nav](https://user-images.githubusercontent.com/32682273/75269335-6a4f1280-5801-11ea-8258-36c42753c555.jpg)
>> ![2_view](https://user-images.githubusercontent.com/32682273/75269339-6b803f80-5801-11ea-8895-ebdcf4030d12.jpg)
>> ![3_sch](https://user-images.githubusercontent.com/32682273/75269340-6c18d600-5801-11ea-8d4e-3ba3b090a5c1.jpg)
>> ![4_cat](https://user-images.githubusercontent.com/32682273/75269343-6c18d600-5801-11ea-9a8d-a994dbacffc9.jpg)

### Schedules
> - A schedule can be composed of 1 or 2 weeks (7 or 14 days). The user can choose the amount of days for each schedule upon creation, but can't change it later on.
> - Only one schedule can be active. Activating a new schedule will automatically deactivate the current active schedule (if it exists).
> - The active schedule is shown in the "Active Schedule" tab of the navigation drawer. Any schedule can be displayed by clicking on it in the "Schedules" tab.
> 
> #### Adding
> ![1_1_schedule_add_7](https://user-images.githubusercontent.com/32682273/75475163-fb102480-59a0-11ea-95f2-62c42cf2db13.gif)
> ![1_2_schedule_add_14](https://user-images.githubusercontent.com/32682273/75475175-fd727e80-59a0-11ea-88e3-ba1ae33e90e4.gif)
> 
> #### Renaming
> ![2_schedule_rename](https://user-images.githubusercontent.com/32682273/75475179-ffd4d880-59a0-11ea-9bbd-84fe1ade6af6.gif)
> 
> #### Deleting
> ![3_schedule_delete](https://user-images.githubusercontent.com/32682273/75475187-02cfc900-59a1-11ea-8b6e-ca50329f70ad.gif)
> 
> #### Changing active schedule
> ![4_schedule_change_active](https://user-images.githubusercontent.com/32682273/75475183-01060580-59a1-11ea-87f6-94beb0b5c195.gif)


### Categories

> #### Adding
> ![5_category_add](https://user-images.githubusercontent.com/32682273/75478057-f39f4a00-59a5-11ea-8676-77da1ba8bb14.gif)
> 
> #### Editing
> ![6_category_edit](https://user-images.githubusercontent.com/32682273/75478059-f437e080-59a5-11ea-8f23-a9178dda504c.gif)
> 
> #### Deleting
> ![7_category_delete](https://user-images.githubusercontent.com/32682273/75478344-7e804480-59a6-11ea-958a-0de2d0caaf0d.gif)

### Events

> #### Adding
> ![8_event_add](https://user-images.githubusercontent.com/32682273/75475198-09f6d700-59a1-11ea-955e-c909f05a114a.gif)
> 
> #### Editing
> ![9_event_delete](https://user-images.githubusercontent.com/32682273/75475206-0e22f480-59a1-11ea-920a-b36ac83c4b24.gif)
> 
> #### Deleting
> ![10_event_delete](https://user-images.githubusercontent.com/32682273/75475213-10854e80-59a1-11ea-8e68-f03707066749.gif)

