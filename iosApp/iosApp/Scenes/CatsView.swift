import SwiftUI

struct CatsView: View {
    @StateObject var store = CatsStore()
    
    var columns: [GridItem] =
            [.init(.adaptive(minimum: 96, maximum: 128))]
    
    var body: some View {
        VStack(spacing: 32) {
            ScrollView {
                LazyVGrid(columns: columns, spacing: 2) {
                    ForEach(store.cats, id: \.id) { cat in
                        CatItem(cat: cat)
                    }
                }
                  .padding(.horizontal, 16)
            }
        }.onAppear {
            store.fetch()
        }
    }
}

struct CatsView_Previews: PreviewProvider {
    static var previews: some View {
        CatsView()
    }
}
