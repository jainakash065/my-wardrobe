# My Wardrobe Product Design

Date: 2026-05-28
Status: Draft for user review

## Purpose

My Wardrobe is a local-first Android app for cataloging clothes from photos and quickly checking what exists in a personal wardrobe. The first target device is a Samsung Galaxy Z Flip 5. The app is for private/local use and is not intended for store publication in the first version.

The product should help the user answer two questions with minimal effort:

- What clothes do I already have?
- Can I quickly find a specific item by photo, category, color, or occasion?

## Product Principles

- Photo-first: the clothing photo is the primary record, not a long form.
- Low-friction capture: adding clothes must feel fast enough for a real wardrobe cleanup session.
- Searchable by default: the home screen should make the wardrobe feel immediately browsable and searchable.
- Local and private: V1 assumes data stays on the device.
- Warm first impression: app launch should include a brief visual moment that feels personal and wardrobe-themed before the utility UI appears.
- Future-ready, not future-heavy: leave room for outfit suggestions and richer metadata later, but do not burden V1.

## V1 Scope

V1 focuses on two jobs:

1. Catalog clothes from photos.
2. Search and browse the catalog quickly.

The app supports both single-item entry and batch capture:

- Single-item entry is for adding one garment carefully.
- Batch capture is for quickly photographing many garments and tagging them later.

## Primary User Experience

The app opens to the Wardrobe screen: a searchable visual grid of saved clothing items. The user can search, browse by category, apply filters, open item details, or tap the camera action to add more clothes.

The main navigation has four core surfaces:

- Wardrobe
- Capture
- Review Queue
- Item Detail

## First-Use Flow

1. Show a short local/private introduction.
2. Ask the user to add the first item or start batch capture.
3. Let the user choose or accept default clothing categories.
4. Land on the Wardrobe screen after the first saved item or skipped setup.

The introduction should be practical and brief. It should not feel like a marketing page.

## Screen Design

### Launch Animation

On every app open, before the Wardrobe screen appears, the app should show a short animation of a girl opening her wardrobe.

Requirements:

- Duration should be about one second.
- The animation should transition smoothly into the Wardrobe screen.
- It should feel warm and personal, not like a generic loading spinner.
- It should not block the app for longer than needed.
- It should be lightweight enough to load quickly on the Samsung Galaxy Z Flip 5.

The implementation can use a Lottie-style animation, a compact vector animation, or another lightweight native animation approach. The exact asset style can be decided during visual design, but the concept is part of V1.

### Wardrobe Home

The Wardrobe screen is the default home screen. It should behave as a dashboard, not a raw dump of every uploaded item. This matters once the wardrobe has hundreds of items.

Core elements:

- Search bar at the top.
- Filter action inside the search field for structured filtering.
- Summary cards for total items, items needing review, and recent additions.
- Recently added section with a compact row of photo cards.
- When search text or filters are active, the dashboard should show matching item results immediately instead of only showing applied filter chips.
- Browse by category section with category-specific illustrated icons and item counts.
- Need review shortcut when incomplete items exist.
- Camera action for adding clothes.

Search should match:

- Name
- Category
- Color
- Occasion
- Fabric
- Season
- Notes

The full photo grid should live in an All Items view that is one tap away from the dashboard through View all actions, category cards, search, or filters. The grid should prioritize photos and short labels. It should support empty, loading, and no-results states.

Future dashboard polish:

- Bottom navigation with Wardrobe, Favorites, Outfits, and Settings.
- Favorites tab for manually marked favorite items.
- Outfits tab for planned outfit combinations.
- Settings tab for label management, backup/export, and preferences.
- Store item creation timestamps so "Added this week" can be accurate instead of using recent item count.
- Category cards should feel like a single unified tile with proper reusable illustration assets (bitmap or vector), not actual wardrobe photos and not hand-drawn code-path sketches, so browsing categories feels stable and intentional.
- Backlog: further polish Browse by category illustrations so every card is instantly recognizable at phone size, with clearer category-specific artwork and less visual ambiguity.

### Capture

The Capture screen supports two modes:

- Add One: take or select one photo, then fill the item form.
- Batch Capture: take or select multiple photos, then send them to the Review Queue.

V1 should support both camera capture and gallery import. Camera capture is useful during wardrobe cleanup, while gallery import keeps the app practical when photos already exist.

Batch capture should avoid forcing metadata entry during the photo-taking session. Its job is speed.

### Review Queue

The Review Queue contains captured photos that are not fully cataloged yet.

Each queued item should show:

- Photo preview
- Missing required fields
- Quick actions to save, skip, or delete

The queue should ask for required fields first, then expose optional fields below. This keeps cleanup focused.

### Item Detail

The Item Detail screen shows and edits one wardrobe item.

It should include:

- Main photo
- Required fields
- Optional fields
- Notes
- Delete action

The screen should answer "what is this?" and "when would I use it?" without becoming a shopping or finance database.

## Data Fields

Required fields:

- Photo
- Name
- Category
- Color

Optional V1 fields:

- Occasion
- Fabric
- Season
- Notes

Future fields:

- Brand
- Purchase date
- Price
- Size
- Care instructions
- Worn count
- Last worn date
- Outfit combinations

## Labels And Category Personalization

The first version can start with fixed category labels, but future versions should make wardrobe labels customizable. Labels are the visible chips and classification values such as All, Saree, Kurti, Top, Dress, and similar groupings.

The app should not assume the wardrobe belongs only to women. During setup or in settings, the user should be able to choose who the wardrobe is for, for example:

- Woman
- Man
- Girl
- Boy
- Child
- General wardrobe

Based on that choice, the app should suggest a sensible default label set. For the first intended user, the default women's set can include:

- Saree
- Kurti
- Top
- Dress
- Dupatta
- Bottom
- Jacket or outerwear
- Accessory
- Other

Users should be able to:

- Add new labels.
- Rename labels.
- Hide labels from the visible chip/filter list.
- Unhide labels later.
- Delete labels only when no wardrobe items are classified under that label.

Important label behavior:

- The All chip is a system view, not a normal user label.
- A hidden label remains valid data and still exists for items already using it.
- Hiding a label should remove it from the normal visible chip list, but it should not delete or reclassify items.
- Deleting a label must be blocked if any item currently uses it.
- If a label is hidden but items use it, those items should still appear in All and search results.
- Settings should clearly distinguish Hide from Delete so users do not fear losing item data.

## Filters

V1 filters should include:

- Category
- Color
- Occasion
- Fabric
- Season

Filters should be optional. The basic search and visual grid should remain useful without meticulous tagging.

Future search improvements should include an advanced filter experience similar to shopping apps such as Myntra. This should let the user combine structured filters, review selected filters as removable chips, clear filters quickly, and browse matching wardrobe items without losing the simple top-level search.

Advanced filters can include:

- Category or custom label
- Color
- Occasion
- Fabric
- Season
- Hidden or visible labels, when customizable labels are implemented
- Future fields such as brand, size, worn count, and last worn date

## Non-Goals For V1

The following are intentionally out of scope for V1:

- Cloud sync
- User accounts
- Social sharing
- Store publication
- AI outfit generation
- Automatic clothing detection from wardrobe shelf photos
- Purchase tracking
- Worn-history analytics

## Roadmap

Future improvements can include:

- Outfit suggestions based on occasion, color, and season.
- Worn history and repeat tracking.
- Rich purchase and care metadata.
- Image-based duplicate detection.
- Backup/export.
- Automatic category or color suggestions from photos.
- Custom wardrobe labels with person-specific defaults, hide/unhide behavior, and guarded delete rules.

## Success Criteria

V1 is successful if:

- A user can add a single clothing item in under one minute.
- A user can batch capture many items without filling forms between photos.
- A user can search for an item by common terms such as "blue saree" or "black kurti".
- The wardrobe grid feels useful with only required fields filled.
- Optional fields improve search without making capture feel heavy.

## Open Decisions For Implementation Planning

- Native Android technology choice.
- Local database choice.
- Image storage strategy.
- How much visual polish to apply before the first usable build.
