import SwiftUI
import shared

struct CatItem: View {
    let cat: Cat
    
    var body: some View {
        VStack {
            AsyncImage(
                ImageService.shared.getImage(url: cat.url)
            ) { image in
                image.resizable()
            }
            .frame(width: 96, height: 96)
        }
    }
}

struct CatItem_Previews: PreviewProvider {
    static var previews: some View {
        CatItem(cat: FakeData.shared.cats[0])
    }
}
